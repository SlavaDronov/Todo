package com.dron.todo.util

import androidx.appcompat.app.AppCompatDelegate
import com.dron.todo.data.local.ThemeMode

object ThemeManager {

    fun applyTheme(mode: Int) {
        val nightMode = when (mode) {
            ThemeMode.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
            ThemeMode.DARK -> AppCompatDelegate.MODE_NIGHT_YES
            else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
        AppCompatDelegate.setDefaultNightMode(nightMode)
    }
}