package ru.kode.plexus.core

class FeatureToggleBuilder {
  private val configs: MutableList<Config> = mutableListOf()

  fun addConfig(config: Config): FeatureToggleBuilder {
    configs.add(config)
    return this
  }

  fun addConfigs(configs: List<Config>): FeatureToggleBuilder {
    this.configs.addAll(configs)
    return this
  }

  fun build(): FeatureToggleManager {
    return FeatureToggleManager(configs)
  }
}
