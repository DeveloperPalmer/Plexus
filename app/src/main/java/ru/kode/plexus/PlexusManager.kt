package ru.kode.plexus

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class PlexusManager internal constructor(private val configs: List<Config>) {

  fun getFeatureValueSync(key: String): String {
    val featureValue = configs.firstNotNullOfOrNull { config ->
      config.getFeatureValueSync(key)
    }
    return featureValue ?: featureNotDeclaredException(key)
  }

  fun getFeatureValue(key: String): Flow<String> {
    val flows = configs.map { config ->
      config.getFeatureValue(key)
    }
    return combine(flows) { configsFeatureValues ->
      configsFeatureValues.firstOrNull() ?: featureNotDeclaredException(key)
    }
  }

  fun getFeaturesValue(keys: List<String>): Flow<Map<String, String>> {
    val flows = configs.map { config ->
      config.getFeatureValues(keys)
    }
    return combine(flows) { configsFeatureValues ->
      keys.associateWith { key ->
        configsFeatureValues.firstNotNullOfOrNull { it[key] } ?: featureNotDeclaredException(key)
      }
    }
  }

  private fun featureNotDeclaredException(key: String): Nothing {
    throw FeatureNotDeclaredException("feature $key is not declared in more than one config")
  }
}