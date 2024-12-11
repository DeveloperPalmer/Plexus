package ru.kode.plexus

import kotlinx.coroutines.flow.Flow

interface Config {
  fun getFeatureValueSync(key: String): FeatureValue
  fun getFeatureValue(key: String): Flow<FeatureValue>
  fun getFeaturesValue(keys: List<String>): Flow<Map<String, FeatureValue>>
}
