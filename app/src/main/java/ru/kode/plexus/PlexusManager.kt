package ru.kode.plexus

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import ru.kode.plexus.FeatureState.Disabled
import ru.kode.plexus.FeatureState.Enabled
import ru.kode.plexus.FeatureState.Undefined

class PlexusManager internal constructor(
  private val configs: List<Config>
) {

  fun getFeatureValueSync(key: String): Boolean {
    return configs.firstNotNullOfOrNull { it.getFeatureStateSync(key).toBooleanOrNull() } ?: false
  }

  fun getFeatureValue(featureKey: String): Flow<Boolean> {
    val flows = configs.map { config ->
      config.getFeatureState(featureKey)
    }
    return combine(flows) { featureStates ->
      featureStates.firstNotNullOfOrNull { it.toBooleanOrNull() } ?: false
    }
  }

  fun getFeaturesValue(vararg featureKeys: String): Flow<Map<String, Boolean>> {
    val flows = configs.map { config ->
      config.getFeaturesState(featureKeys.toList())
    }
    return combine(flows) { result ->
      featureKeys.associateWith { key ->
        result.firstNotNullOfOrNull { it[key]?.toBooleanOrNull() } ?: false
      }
    }
  }

  private fun FeatureState.toBooleanOrNull(): Boolean? {
    return when (this) {
      Enabled -> true
      Disabled -> false
      Undefined -> null
    }
  }
}