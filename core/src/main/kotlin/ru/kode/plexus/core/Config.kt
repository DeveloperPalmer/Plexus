package ru.kode.plexus.core

import kotlinx.coroutines.flow.Flow

interface Config {
  fun getFeatureValueSync(key: String): String?
  fun getFeatureValue(key: String): Flow<String?>
  fun getFeatureValues(keys: List<String>): Flow<Map<String, String?>>
}
