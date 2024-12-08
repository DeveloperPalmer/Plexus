package ru.kode.plexus

class PlexusBuilder {
  private val configs: MutableList<Config> = mutableListOf()

  fun addConfig(config: Config): PlexusBuilder {
    configs.add(config)
    return this
  }

  fun addConfigs(configs: List<Config>): PlexusBuilder {
    this.configs.addAll(configs)
    return this
  }

  fun build(): PlexusManager {
    return PlexusManager(configs)
  }
}

fun plexusManager(build: PlexusBuilder.() -> Unit): PlexusManager {
  return PlexusBuilder().apply(build).build()
}