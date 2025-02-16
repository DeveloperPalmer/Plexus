package ru.kode.plexus.core

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class FeatureConfigsManager internal constructor(private val configs: List<Config>) {

  fun getFeatureValueSync(key: String): String {
    val featureValue = configs.firstNotNullOfOrNull { config ->
      config.getValueSync(key)
    }
    return featureValue ?: featureNotFoundException(key)
  }

  fun getFeatureValue(key: String): Flow<String> {
    val flows = configs.map { config ->
      config.getValue(key)
    }
    return combine(flows) { configsFeatureValues ->
      configsFeatureValues.firstNotNullOfOrNull { it } ?: featureNotFoundException(key)
    }
  }

  fun getFeaturesValue(keys: List<String>): Flow<Map<String, String>> {
    val flows = configs.map { config ->
      config.getValues(keys)
    }
    return combine(flows) { configsFeatureValues ->
      keys.associateWith { key ->
        configsFeatureValues.firstNotNullOfOrNull { it[key] } ?: featureNotFoundException(key)
      }
    }
  }

  private fun featureNotFoundException(key: String): Nothing {
    throw FeatureNotDeclaredException("Feature key: $key not found of any configs")
  }
}
