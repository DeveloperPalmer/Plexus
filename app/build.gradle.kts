plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.jetbrains.kotlin.android)
  alias(libs.plugins.sqldelight)
}

android {
  namespace = "ru.kode.plexus"
  compileSdk = 34

  defaultConfig {
    applicationId = "ru.kode.plexus"
    minSdk = 24
    targetSdk = 34
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
  kotlinOptions {
    jvmTarget = "1.8"
  }
}

sqldelight {
  databases {
    create("FeatureDB") {
      packageName = "ru.kode.plexus.database"
      srcDirs("src/main/sqldelight/feature")
    }
  }
}

dependencies {
  implementation(libs.androidx.core.ktx)
  implementation(libs.material)

  implementation(libs.sqldelight.android.driver)
  api(libs.sqldelight.coroutines)
}