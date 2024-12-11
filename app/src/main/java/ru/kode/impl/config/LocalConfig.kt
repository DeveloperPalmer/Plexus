package ru.kode.impl.config

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import ru.kode.impl.AppFeature
import ru.kode.plexus.Config
import ru.kode.plexus.FeatureValue

class LocalConfig: Config {
  override fun getFeatureValueSync(key: String): FeatureValue {
    return features.getOrDefault(key, FeatureValue.Undefined)
  }

  override fun getFeatureValue(key: String): Flow<FeatureValue> {
    return flowOf(features.getOrDefault(key, FeatureValue.Undefined))
  }

  override fun getFeaturesValue(keys: List<String>): Flow<Map<String, FeatureValue>> {
    return flowOf(features.filter { it.key in keys })
  }

  private val features: Map<String, FeatureValue> = mapOf(
    AppFeature.Name.key to FeatureValue.StringValue("Aboba"),
    AppFeature.IsRich.key to FeatureValue.BooleanValue(false),
    AppFeature.Age.key to FeatureValue.LongValue(10L),
    AppFeature.Money.key to FeatureValue.DoubleValue(10.99)
  )
}
