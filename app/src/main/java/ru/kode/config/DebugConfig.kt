package ru.kode.config

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import ru.kode.plexus.core.Config

class DebugConfig(context: Context) : Config {

  private val preferences = context.getSharedPreferences(
    "debug_config_features",
    Context.MODE_PRIVATE
  )

  private val triggerRelay = MutableSharedFlow<Unit>(
    replay = 1
  )

  override fun getFeatureValueSync(key: String): String? {
    return preferences.getString(key, null)
  }

  override fun getFeatureValue(key: String): Flow<String?> {
    return triggerRelay
      .onStart { emit(Unit) }
      .map { preferences.getString(key, null) }
  }

  override fun getFeatureValues(keys: List<String>): Flow<Map<String, String?>> {
    return triggerRelay
      .onStart { emit(Unit) }
      .map { keys.associateWith { preferences.getString(it, null) } }
  }

  fun setFeatureValue(key: String, value: String) {
    preferences.edit()
      .putString(key, value)
      .apply()
    triggerRelay.tryEmit(Unit)
  }
}
