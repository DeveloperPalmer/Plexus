package ru.kode.plexus

sealed interface FeatureValue {
  data class StringValue(val value: String): FeatureValue
  data class BooleanValue(val value: Boolean): FeatureValue
  data class LongValue(val value: Long): FeatureValue
  data class DoubleValue(val value: Double): FeatureValue
  data object Undefined: FeatureValue
}
