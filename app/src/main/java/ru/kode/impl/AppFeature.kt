package ru.kode.impl

import kotlin.reflect.KClass

sealed class AppFeature<out T : Any>(
  val key: String,
  val type: KClass<@UnsafeVariance T>
) {
  data object Name : AppFeature<String>(
    key = "name_key",
    type = String::class
  )

  data object IsRich : AppFeature<Boolean>(
    key = "is_rich_key",
    type = Boolean::class
  )

  data object Age : AppFeature<Long>(
    key = "age_key",
    type = Long::class
  )

  data object Money : AppFeature<Double>(
    key = "money_key",
    type = Double::class
  )
}
