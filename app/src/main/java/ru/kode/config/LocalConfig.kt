package ru.kode.config

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import ru.kode.AppFeature
import ru.kode.plexus.core.Config

class LocalConfig: Config {
  override fun getFeatureValueSync(key: String): String? {
    return features[key]
  }

  override fun getFeatureValue(key: String): Flow<String?> {
    return flowOf(features[key])
  }

  override fun getFeatureValues(keys: List<String>): Flow<Map<String, String?>> {
    return flowOf(keys.associateWith { features[it] })
  }

  private val features: Map<String, String> = mapOf(
    AppFeature.Name.key to "Local",
    AppFeature.IsRich.key to "true",
    AppFeature.Money.key to "1.0",
    AppFeature.Age.key to "1",
  )
}
