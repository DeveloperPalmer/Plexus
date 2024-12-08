package ru.kode

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.kode.impl.AppFeature
import ru.kode.impl.config.DebugConfig
import ru.kode.impl.config.LocalConfig
import ru.kode.impl.config.RemoteConfig
import ru.kode.plexus.plexusManager

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    val scope = CoroutineScope(Dispatchers.IO)


    val manager = plexusManager {
      addConfig(DebugConfig())
      addConfig(RemoteConfig {})
      addConfig(LocalConfig())
    }

    val mainToggle = manager.getFeatureValueSync(AppFeature.MainFeature.key)
    Log.d("FirebaseRemoteConfig", "MainFeature -> getFeatureStateSync($mainToggle)")

    val secondaryToggle = manager.getFeatureValueSync(AppFeature.SecondaryFeature.key)
    Log.d("FirebaseRemoteConfig", "SecondaryFeature -> getFeatureStateSync($secondaryToggle)")

    manager
      .getFeatureValue(AppFeature.MainFeature.key)
      .onEach { value ->
        Log.d("FirebaseRemoteConfig", "one ->getFeatureState($value)")
      }
      .launchIn(scope)

    manager
      .getFeaturesValue(
        AppFeature.MainFeature.key,
        AppFeature.SecondaryFeature.key,
        AppFeature.TertiaryFeature.key
      )
      .onEach { value ->
        Log.d("FirebaseRemoteConfig", "many -> getFeaturesState($value)")
      }
      .launchIn(scope)
  }
}
