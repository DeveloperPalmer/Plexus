package ru.kode

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.kode.config.DebugConfig
import ru.kode.config.LocalConfig
import ru.kode.config.FirebaseConfig
import ru.kode.plexus.core.FeatureConfigsBuilder


class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      val scope = rememberCoroutineScope()
      val debugConfig = remember { DebugConfig(applicationContext) }
      var debugFeatures by remember { mutableStateOf<List<DebugItem>>(emptyList()) }

      val plexusManager = remember {
        FeatureConfigsBuilder()
          .addConfig(debugConfig)
          .addConfig(FirebaseConfig {})
          .addConfig(LocalConfig())
          .build()
      }

      LaunchedEffect(Unit) {
        plexusManager.getFeaturesValue(
          listOf(
            AppFeature.Name.key,
            AppFeature.IsRich.key,
            AppFeature.Age.key,
            AppFeature.Money.key,
          )
        ).onEach { features ->
          debugFeatures = features.map { (feature, value) -> feature.toUiModel(value) }
        }.launchIn(scope)
      }

      Column(
        modifier = Modifier
          .systemBarsPadding()
          .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        debugFeatures.forEach { item ->
          when (item) {
            is DebugItem.DoubleValue -> {
              DoubleItem(
                name = item.key,
                currentValue = item.value,
                values = remember { listOf(25.0, 50.0, 75.0, 100.0) },
                onValueChange = { debugConfig.setValue(item.key, it.toString()) }
              )
            }

            is DebugItem.LongValue -> {
              LongItem(
                name = item.key,
                currentValue = item.value,
                values = remember { listOf(25L, 50L, 75L, 100L) },
                onValueChange = { debugConfig.setValue(item.key, it.toString()) }
              )
            }

            is DebugItem.BooleanValue -> {
              BooleanItem(
                name = item.key,
                checked = item.value,
                onCheckedChange = { debugConfig.setValue(item.key, it.toString()) }
              )
            }

            is DebugItem.StringValue -> {
              var temp by remember(item.value) { mutableStateOf(item.value) }
              StringItem(
                name = item.key,
                value = temp,
                onApply = { debugConfig.setValue(item.key, temp) },
                onValueChange = { temp = it }
              )
            }
          }
        }
      }
    }
  }


  private fun String.toUiModel(value: String): DebugItem {
    return when (this) {
      AppFeature.Age.key -> DebugItem.LongValue(
        key = AppFeature.Age.key,
        value = value.toLong()
      )

      AppFeature.IsRich.key -> DebugItem.BooleanValue(
        key = AppFeature.IsRich.key,
        value = value.toBoolean()
      )

      AppFeature.Money.key -> DebugItem.DoubleValue(
        key = AppFeature.Money.key,
        value = value.toDouble()
      )

      AppFeature.Name.key -> DebugItem.StringValue(
        key = AppFeature.Name.key,
        value = value
      )

      else -> error("Unexpected feature: $this")
    }
  }

  @Composable
  private fun StringItem(
    name: String,
    value: String,
    onValueChange: (String) -> Unit,
    onApply: () -> Unit,
    modifier: Modifier = Modifier,
  ) {
    Column(modifier = modifier) {
      Text(
        text = name,
        color = MaterialTheme.colorScheme.onPrimary,
        style = MaterialTheme.typography.bodyMedium
      )
      Spacer(Modifier.height(16.dp))
      TextField(
        value = value,
        onValueChange = onValueChange
      )
      Spacer(Modifier.height(8.dp))
      Button(onClick = onApply) {
        Text(
          text = "Применить",
          color = MaterialTheme.colorScheme.onPrimary,
          style = MaterialTheme.typography.bodyMedium
        )
      }
    }
  }

  @Composable
  private fun BooleanItem(
    name: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
  ) {
    Row(modifier = modifier) {
      Text(
        text = name,
        color = MaterialTheme.colorScheme.onPrimary,
        style = MaterialTheme.typography.bodyMedium
      )
      Spacer(Modifier.weight(1f))
      Switch(
        checked = checked,
        onCheckedChange = { onCheckedChange(it) }
      )
    }
  }

  @Composable
  private fun DoubleItem(
    name: String,
    currentValue: Double,
    values: List<Double>,
    onValueChange: (Double) -> Unit,
    modifier: Modifier = Modifier,
  ) {
    Column(modifier = modifier) {
      Text(
        text = name,
        color = MaterialTheme.colorScheme.onPrimary,
        style = MaterialTheme.typography.bodyMedium
      )
      Spacer(Modifier.height(16.dp))
      Text(
        text = "($currentValue)",
        color = MaterialTheme.colorScheme.onPrimary,
        style = MaterialTheme.typography.bodyMedium
      )
      Spacer(Modifier.height(16.dp))
      LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        items(values) { value ->
          Text(
            modifier = Modifier
              .background(
                MaterialTheme.colorScheme.primary,
                MaterialTheme.shapes.medium
              )
              .clickable { onValueChange(value) }
              .padding(8.dp),
            text = value.toString(),
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.bodyMedium
          )
        }
      }
    }
  }

  @Composable
  private fun LongItem(
    name: String,
    currentValue: Long,
    values: List<Long>,
    onValueChange: (Long) -> Unit,
    modifier: Modifier = Modifier,
  ) {
    Column(modifier = modifier) {
      Text(
        text = name,
        color = MaterialTheme.colorScheme.onPrimary,
        style = MaterialTheme.typography.bodyMedium
      )
      Spacer(Modifier.height(16.dp))
      Text(
        text = "($currentValue)",
        color = MaterialTheme.colorScheme.onPrimary,
        style = MaterialTheme.typography.bodyMedium
      )
      Spacer(Modifier.height(16.dp))
      LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        items(values) { value ->
          Text(
            modifier = Modifier
              .background(
                MaterialTheme.colorScheme.primary,
                MaterialTheme.shapes.medium
              )
              .clickable { onValueChange(value) }
              .padding(8.dp),
            text = value.toString(),
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.bodyMedium
          )
        }
      }
    }
  }
}
