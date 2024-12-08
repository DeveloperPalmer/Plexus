package ru.kode.plexus

import kotlinx.coroutines.flow.Flow

interface Config {
  fun getFeatureStateSync(key: String): FeatureState
  fun getFeatureState(key: String): Flow<FeatureState>
  fun getFeaturesState(keys: List<String>): Flow<Map<String, FeatureState>>
}
