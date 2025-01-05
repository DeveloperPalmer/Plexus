package ru.kode

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.kode.impl.AppFeature
import ru.kode.impl.DebugItem

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
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
