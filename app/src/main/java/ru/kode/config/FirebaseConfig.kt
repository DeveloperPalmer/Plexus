package ru.kode.config

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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import ru.kode.plexus.core.Config

class FirebaseConfig(onComplete: () -> Unit) : Config {

  private val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
  private val initialized = MutableStateFlow(false)

  init {
    remoteConfig.setConfigSettingsAsync(
      remoteConfigSettings {
        minimumFetchIntervalInSeconds = MIN_FETCH_INTERVAL_SECONDS
        fetchTimeoutInSeconds = FETCH_TIMEOUT_SECONDS
      }
    )
    remoteConfig.ensureInitialized().addOnSuccessListener {
      initialized.value = true
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
    remoteConfig.fetchAndActivate().addOnCompleteListener {
      onComplete()
    }
  }

  override fun getFeatureValueSync(key: String): String? {
    return remoteConfig[key].toFeatureValue()
  }

  override fun getFeatureValue(key: String): Flow<String?> {
    return merge(
      flow {
        emit(remoteConfig[key].toFeatureValue())
      },
      remoteConfig.configUpdates
        .filter { it.updatedKeys.contains(key) }
        .map { remoteConfig[key].toFeatureValue() }
        .distinctUntilChanged()
    )
  }

  override fun getFeatureValues(keys: List<String>): Flow<Map<String, String?>> {
    return merge(
      flow {
        emit(getFeatureValuesByKeys(keys))
      },
      remoteConfig.configUpdates
        .map { getFeatureValuesByKeys(keys) }
        .distinctUntilChanged()
    )
  }

  private fun getFeatureValuesByKeys(keys: List<String>): Map<String, String?> {
    return remoteConfig.all
      .filter { it.key in keys }
      .mapValues { it.value.toFeatureValue() }
  }

  private fun FirebaseRemoteConfigValue.toFeatureValue(): String? {
    return when {
      !initialized.value -> null
      source != FirebaseRemoteConfig.VALUE_SOURCE_REMOTE -> null
      else -> asString()
    }
  }
}

private const val MIN_FETCH_INTERVAL_SECONDS = 10L
private const val FETCH_TIMEOUT_SECONDS = 10L
