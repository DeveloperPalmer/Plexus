package ru.kode.impl.config

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import ru.kode.impl.AppFeature
import ru.kode.plexus.Config
import ru.kode.plexus.FeatureState

class LocalConfig: Config {
  override fun getFeatureStateSync(key: String): FeatureState {
    return features.getOrDefault(key, FeatureState.Undefined)
  }

  override fun getFeatureState(key: String): Flow<FeatureState> {
    return flowOf(features.getOrDefault(key, FeatureState.Undefined))
  }

  override fun getFeaturesState(keys: List<String>): Flow<Map<String, FeatureState>> {
    return flowOf(features.filter { it.key in keys })
  }

  private val features: Map<String, FeatureState> = mapOf(
    AppFeature.MainFeature.key to FeatureState.Enabled,
    AppFeature.SecondaryFeature.key to FeatureState.Disabled,
    AppFeature.TertiaryFeature.key to FeatureState.Enabled,
  )
}