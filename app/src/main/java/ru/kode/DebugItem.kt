package ru.kode

sealed interface DebugItem {
  val key: String

  data class StringValue(
    override val key: String,
    val value: String,
  ) : DebugItem

  data class BooleanValue(
    override val key: String,
    val value: Boolean,
  ) : DebugItem

  data class LongValue(
    override val key: String,
    val value: Long,
  ) : DebugItem

  data class DoubleValue(
    override val key: String,
    val value: Double,
  ) : DebugItem
}
