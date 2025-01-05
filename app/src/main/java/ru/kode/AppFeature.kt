package ru.kode

sealed class AppFeature(val key: String) {
  data object Age: AppFeature(key = "Age")
  data object IsRich: AppFeature(key = "IsRich")
  data object Money: AppFeature(key = "Money")
  data object Name: AppFeature(key = "Name")
}
