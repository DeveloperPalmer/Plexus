package ru.kode.plexus

internal fun <T> Comparable<T>.toFeatureValue(): FeatureValue {
  return when (this) {
    is Boolean -> FeatureValue.BooleanValue(this)
    is String -> FeatureValue.StringValue(this)
    is Long -> FeatureValue.LongValue(this)
    is Double -> FeatureValue.DoubleValue(this)
    else -> error("unexpected type of value")
  }
}
