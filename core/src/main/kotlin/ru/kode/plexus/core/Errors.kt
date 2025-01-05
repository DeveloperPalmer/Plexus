package ru.kode.plexus.core

class FeatureNotDeclaredException(
  override val message: String?,
  override val cause: Throwable? = null
) : RuntimeException(message, cause)
