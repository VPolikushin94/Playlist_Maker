package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.di.dataModule
import com.example.playlistmaker.di.interactorModule
import com.example.playlistmaker.di.repositoryModule
import com.example.playlistmaker.di.viewModelModule
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin



class App : Application() {

    var isDarkTheme = false
    private val themeSharedPrefs: SharedPreferences by inject()

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(dataModule, repositoryModule, interactorModule, viewModelModule)
        }

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