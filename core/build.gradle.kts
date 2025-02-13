plugins {
  alias(libs.plugins.jetbrains.kotlin.jvm)
  `maven-publish`
}

kotlin {
  jvmToolchain {
    languageVersion = JavaLanguageVersion.of(11)
  }
}

dependencies {
  implementation(libs.kotlin.coroutines.core)
}

publishing {
  publications {
    create<MavenPublication>("release") {
      groupId = "ru.kode.plexus"
      artifactId = "core"
      version = "1.0"

      afterEvaluate {
        from(components["java"])
      }

      pom {
        name.set("Sergey")
        description.set("A library for configure and managment feature configs")

        developers {
          developer {
            id.set("@sla")
            name.set("Lanovoy Sergey")
          }
        }
      }
    }
  }

  repositories {
    mavenLocal()
    maven {
      name = "BuildDir"
      url = uri(rootProject.layout.buildDirectory.dir("plexus-deploy"))
    }
  }
}
