package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    var isDarkTheme = false
    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreate() {
        super.onCreate()

        sharedPrefs = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE)
        isDarkTheme = sharedPrefs.getBoolean(DARK_THEME, isDarkThemeDefault())
        switchTheme(isDarkTheme)
    }

    fun switchTheme(isDarkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if(isDarkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        isDarkTheme = isDarkThemeEnabled
        sharedPrefs.edit()
            .putBoolean(DARK_THEME, isDarkTheme)
            .apply()
    }

    private fun isDarkThemeDefault(): Boolean {
        return resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
    }

    companion object {
        private const val SHARED_PREFERENCES = "SHARED_PREFERENCES"
        private const val DARK_THEME = "DARK_THEME"
    }
}