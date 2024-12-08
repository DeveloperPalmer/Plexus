package ru.kode.impl.config

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import ru.kode.impl.AppFeature
import ru.kode.plexus.Config
import ru.kode.plexus.FeatureState

class DebugConfig : Config {

  private val stateFlow = MutableStateFlow(
    mapOf(
      AppFeature.MainFeature.key to FeatureState.Disabled,
    )
  )

  override fun getFeatureStateSync(key: String): FeatureState {
    return stateFlow.value[key] ?: FeatureState.Undefined
  }

  override fun getFeatureState(key: String): Flow<FeatureState> {
    return stateFlow.map { it[key] ?: FeatureState.Undefined }
  }

  override fun getFeaturesState(keys: List<String>): Flow<Map<String, FeatureState>> {
    return stateFlow.map { features -> features.filter { it.key in keys } }
  }
}
