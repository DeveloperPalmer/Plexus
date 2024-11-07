package ru.kode.plexus

import kotlinx.coroutines.flow.Flow

interface FeatureManager {

  val features: Flow<Map<Feature, String>>

  /**
   * Subscribe to the updated value using the [feature] key
   * @param feature key that will be used to get the value
   * */
  fun <T : Any> featureValue(feature: Feature): Flow<T>

  /**
   * Get value by [feature] key
   * @param feature key that will be used to get the value
   * */
  fun <T : Any> featureValueSync(feature: Feature): T

  /**
   * Reset the [features] to the values of the [FeatureSource] config
   * */
  fun resetConfig()

  /**
   * Set a new feature [value] by [feature]
   * @param feature the key by which the new value will be replaced
   * @param value updated value
   * */
  fun setFeature(feature: Feature, value: Any)

  /**
   * Download the current state from a remote source
   * */
  suspend fun fetchSource()
}
