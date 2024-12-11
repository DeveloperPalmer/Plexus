package ru.kode.plexus

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import ru.kode.impl.AppFeature
import kotlin.reflect.cast

class PlexusManager internal constructor(
  val configs: List<Config>,
) {

  inline fun <reified T : Any> getFeatureValueSync(appFeature: AppFeature<T>): T {
    return configs.firstNotNullOfOrNull {
      it.getFeatureValueSync(appFeature.key).toValueOrNull(appFeature)
    } ?: throw FeatureNotDeclaredException("AppFeature $appFeature is not declared in more than one config")
  }

  inline fun <reified T : Any> getFeatureValue(appFeature: AppFeature<T>): Flow<T> {
    val flows = configs.map { config ->
      config.getFeatureValue(appFeature.key)
    }
    return combine(flows) { featureValues ->
      featureValues.firstNotNullOfOrNull { it.toValueOrNull(appFeature) }
    }.map { result ->
      result ?: throw FeatureNotDeclaredException("AppFeature $appFeature is not declared in more than one config")
    }
  }

  inline fun <reified T : Any> getFeaturesValue(vararg appFeatures: AppFeature<T>): Flow<Map<AppFeature<T>, T>> {
    val keys = appFeatures.map { it.key }
    val flows = configs.map { config ->
      config.getFeaturesValue(keys)
    }
    return combine(flows) { featuresValues ->
      appFeatures.associateWith { appFeature ->
        featuresValues.firstNotNullOfOrNull { it[appFeature.key]?.toValueOrNull(appFeature) }
      }
    }.map {
      it.mapValues { (appFeature, result) ->
        result ?: throw FeatureNotDeclaredException("AppFeature $appFeature is not declared in more than one config")
      }
    }
  }

  inline fun <reified T : Any> FeatureValue.toValueOrNull(appFeature: AppFeature<T>): T? {
    return when (this) {
      is FeatureValue.LongValue -> appFeature.type.cast(value)
      is FeatureValue.DoubleValue -> appFeature.type.cast(value)
      is FeatureValue.StringValue -> appFeature.type.cast(value)
      is FeatureValue.BooleanValue -> appFeature.type.cast(value)
      is FeatureValue.Undefined -> null
    }
  }
}
