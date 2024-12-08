package ru.kode.plexus

class FeatureNotDeclaredException(
  override val message: String?,
  override val cause: Throwable? = null
) : RuntimeException(message, cause)
