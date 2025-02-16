
# Feature Configs Library

Библиотека для формирования конфигураций, которые содержат список пар ключ-значение. Она предоставляет удобный API для доступа к значениям этих конфигураций через класс `FeatureConfigsManager`.

## Установка

Для добавления библиотеки в ваш проект, добавьте следующий dependency в ваш `build.gradle` файл:

```groovy
dependencies {
    implementation 'ru.kode.plexus:core:1.0'
}
```

## Использование

Для создания экземпляра `FeatureConfigsManager`, используйте `FeatureConfigsBuilder`. Вы можете добавлять различные конфигурации перед сборкой менеджера:

```kotlin
val configsManager = FeatureConfigsBuilder()
    .addConfig(DebugConfig())
    .addConfig(FirebaseConfig()
    .addConfig(LocalConfig())
    .build()
 ```
## Доступ к значениям

`FeatureConfigsManager` предоставляет следующие методы для получения значений:

1. **Синхронное получение значения**:
   ```kotlin
   val value: String = configsManager.getFeatureValueSync("your_key")
   ```
2. **Асинхронное получение значения с использованием Flow**:
   ```kotlin
   val flowValue: Flow<String> = configsManager.getFeatureValue("your_key")
   ```
3. **Асинхронное получение значений для нескольких ключей**:
   ```kotlin
   val flowValues: Flow<Map<String, String>> = configsManager.getFeaturesValue(listOf("your_key1", "your_key2"))
   ```

## Правила получения значений
1. Получение значения осуществляется с учетом порядка конфигурации при добавлении в менеджер. Это значит, что значения из конфигурации, добавленной первой, будет получено в порядке приорита и т.д.
2. Если для указанного ключа не найдено соответствующее значение во всех конфигурациях, будет выброшено исключение.
