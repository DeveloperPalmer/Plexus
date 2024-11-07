package ru.kode.plexus

interface FeatureSource {
  /**
   * The name of the configuration
   * */
  val name: String

  /**
   * Get value by [feature] key
   * @param feature key that will be used to get the value
   * */
  fun get(feature: Feature): Any?

  /**
   * Fetching actual data from the third-party service
   * */
  suspend fun fetch()
}
