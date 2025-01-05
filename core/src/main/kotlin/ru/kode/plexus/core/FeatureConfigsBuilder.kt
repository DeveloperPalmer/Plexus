package ru.kode.plexus.core

import java.util.concurrent.CopyOnWriteArrayList

class FeatureConfigsBuilder {

  private val configs: CopyOnWriteArrayList<Config> = CopyOnWriteArrayList()

  fun addConfig(config: Config): FeatureConfigsBuilder {
    configs.add(config)
    return this
  }

  fun addConfigs(configs: List<Config>): FeatureConfigsBuilder {
    this.configs.addAll(configs)
    return this
  }

  fun build(): FeatureConfigsManager {
    return FeatureConfigsManager(configs)
  }
}
