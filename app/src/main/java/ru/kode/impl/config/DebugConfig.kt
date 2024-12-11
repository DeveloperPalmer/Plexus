package ru.kode.impl.config

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import ru.kode.plexus.Config
import ru.kode.plexus.FeatureValue

class DebugConfig(context: Context) : Config {

  private val preferences = context.getSharedPreferences(
    "debug_config_features_state",
    Context.MODE_PRIVATE
  )

  private val triggerRelay = MutableSharedFlow<Unit>(
    replay = 1
  )

  override fun getFeatureValueSync(key: String): FeatureValue {
    return getFeatureValueInternal(key)
  }

  override fun getFeatureValue(key: String): Flow<FeatureValue> {
    return triggerRelay
      .onStart { emit(Unit) }
      .map { getFeatureValueInternal(key) }
  }

  override fun getFeaturesValue(keys: List<String>): Flow<Map<String, FeatureValue>> {
    return triggerRelay
      .onStart { emit(Unit) }
      .map { keys.associateWith { feature -> getFeatureValueInternal(feature) } }
  }

  fun setFeatureEnabledState(key: String, featureValue: FeatureValue) {
    preferences.edit()
      .apply {
        when (featureValue) {
          is FeatureValue.BooleanValue -> putString(key, featureValue.value.toString())
          is FeatureValue.DoubleValue -> putString(key, featureValue.value.toString())
          is FeatureValue.LongValue -> putString(key, featureValue.value.toString())
          is FeatureValue.StringValue -> putString(key, featureValue.value)
          is FeatureValue.Undefined -> Unit
        }
      }
      .apply()
    triggerRelay.tryEmit(Unit)
  }

  private fun getFeatureValueInternal(featureId: String): FeatureValue {
    val targetValue = preferences.getString(featureId, null)
    val b1 = targetValue?.toBoolean()?.let(FeatureValue::BooleanValue)
    val l1 = targetValue?.toLongOrNull()?.let(FeatureValue::LongValue)
    val d1 = targetValue?.toDoubleOrNull()?.let(FeatureValue::DoubleValue)
    val s1 = targetValue?.let(FeatureValue::StringValue)
    return b1 ?: l1 ?: d1 ?: s1 ?: FeatureValue.Undefined
  }

  private fun String.toBoolean(): Boolean? {
    return when (this) {
      "true" -> true
      "false"-> false
      else -> null
    }
  }
}
