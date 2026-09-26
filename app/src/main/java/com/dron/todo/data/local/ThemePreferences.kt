package com.dron.todo.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "theme_prefs")

class ThemePreferences(private val context: Context) {

    companion object {
        private val THEME_KEY = intPreferencesKey("theme_mode")
    }

    val themeMode: Flow<Int> = context.dataStore.data
        .map { prefs -> prefs[THEME_KEY] ?: ThemeMode.SYSTEM }

    suspend fun setThemeMode(mode: Int) {
        context.dataStore.edit { prefs ->
            prefs[THEME_KEY] = mode
        }
    }
}

object ThemeMode {
    const val LIGHT = 0
    const val DARK = 1
    const val SYSTEM = 2
}