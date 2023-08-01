package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.util.SharedPrefsManager


class App : Application() {

    var isDarkTheme = false
    private lateinit var themeSharedPrefs: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        SharedPrefsManager.init(applicationContext, SharedPrefsManager.THEME_SHARED_PREFERENCES)
        SharedPrefsManager.init(applicationContext, SharedPrefsManager.SEARCH_SHARED_PREFERENCES)

        themeSharedPrefs = SharedPrefsManager.themeInstance ?: throw RuntimeException("Theme shared prefs did not init")
        isDarkTheme = themeSharedPrefs.getBoolean(DARK_THEME, isDarkThemeDefault())
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
        themeSharedPrefs.edit()
            .putBoolean(DARK_THEME, isDarkTheme)
            .apply()
    }

    private fun isDarkThemeDefault(): Boolean {
        return resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
    }

    companion object {
        private const val DARK_THEME = "DARK_THEME"
    }
}