// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
  alias(libs.plugins.android.application) apply false
  alias(libs.plugins.jetbrains.kotlin.android) apply false
  alias(libs.plugins.google.gms.google.services) apply false
}

// Функция для рекурсивного добавления зависимостей по имени
fun Task.dependsOnRecursivelyByName(name: String) {
    project.subprojects.forEach { subproject ->
        subproject.tasks.matching { it.name == name }.forEach { t ->
            dependsOn(t)
        }
    }
}

// Регистрация задачи prePushCheck
tasks.register<Task>("prePushCheck") {
    group = "verification"
    // Добавление зависимостей
    dependsOnRecursivelyByName("spotlessApply")
    dependsOnRecursivelyByName("detektDebug")
}