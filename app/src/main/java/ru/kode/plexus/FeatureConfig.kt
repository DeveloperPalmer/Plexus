package ru.kode.plexus

interface FeatureConfig {

  /**
   * The name of the configuration
   * */
  val name: String

  /**
   * Set of features to which values obtained from the source should be assigned
   * */
  val features: Set<Feature>
}
