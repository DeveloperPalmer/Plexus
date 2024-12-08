package ru.kode.impl.config

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.google.firebase.remoteconfig.FirebaseRemoteConfigValue
import com.google.firebase.remoteconfig.configUpdates
import com.google.firebase.remoteconfig.get
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import ru.kode.plexus.Config
import ru.kode.plexus.FeatureState

class RemoteConfig(onComplete: () -> Unit) : Config {

  private val remoteConfig: FirebaseRemoteConfig by lazy { Firebase.remoteConfig }

  private val configSettings by lazy {
    remoteConfigSettings {
      minimumFetchIntervalInSeconds = MIN_FETCH_INTERVAL_SECONDS
      fetchTimeoutInSeconds = FETCH_TIMEOUT_SECONDS
    }
  }

  init {
    remoteConfig.setConfigSettingsAsync(configSettings)
    remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
      if (task.isSuccessful) {
        onComplete.invoke()
      } else {
        onComplete.invoke()
      }
    }
    remoteConfig.ensureInitialized().addOnSuccessListener {
      val listener = object : ConfigUpdateListener {
        override fun onUpdate(configUpdate: ConfigUpdate) {
          remoteConfig.activate()
        }
        override fun onError(error: FirebaseRemoteConfigException) {
          // do nothing
        }
      }
      remoteConfig.addOnConfigUpdateListener(listener)
    }
  }

  override fun getFeatureStateSync(key: String): FeatureState {
    return remoteConfig[key].toFeatureState()
  }

  override fun getFeatureState(key: String): Flow<FeatureState> {
    return merge(
      flow {
        emit(remoteConfig[key].toFeatureState())
      },
      remoteConfig.configUpdates
        .filter { it.updatedKeys.contains(key) }
        .map { remoteConfig[key].toFeatureState() }
        .distinctUntilChanged()
    )
  }

  override fun getFeaturesState(keys: List<String>): Flow<Map<String, FeatureState>> {
    return merge(
      flow {
        emit(getFeatureStatesByKeys(keys))
      },
      remoteConfig.configUpdates
        .map { getFeatureStatesByKeys(keys) }
        .distinctUntilChanged()
    )
  }

  private fun getFeatureStatesByKeys(keys: List<String>): Map<String, FeatureState> {
    return remoteConfig.all
      .filter { it.key in keys }
      .mapValues { it.value.toFeatureState() }
  }

  private fun FirebaseRemoteConfigValue.toFeatureState(): FeatureState {
    return when {
      source != FirebaseRemoteConfig.VALUE_SOURCE_REMOTE -> FeatureState.Undefined
      else -> if (asBoolean()) FeatureState.Enabled else FeatureState.Disabled
    }
  }
}

private const val MIN_FETCH_INTERVAL_SECONDS = 10L
private const val FETCH_TIMEOUT_SECONDS = 10L