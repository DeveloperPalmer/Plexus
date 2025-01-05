package ru.kode.plexus.core

import kotlinx.coroutines.flow.Flow

interface Config {
  fun getValueSync(key: String): String?
  fun getValue(key: String): Flow<String?>
  fun getValues(keys: List<String>): Flow<Map<String, String?>>
}
