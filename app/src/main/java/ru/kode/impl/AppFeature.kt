package ru.kode.impl

sealed class AppFeature(override val key: String): Feature {
  data object MainFeature: AppFeature(key = "Main")
  data object SecondaryFeature: AppFeature(key = "Secondary")
  data object TertiaryFeature: AppFeature(key = "Tertiary")
}
