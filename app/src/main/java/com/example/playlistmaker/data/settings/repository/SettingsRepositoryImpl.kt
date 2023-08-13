package com.example.playlistmaker.data.settings.repository

import android.content.Context
import com.example.playlistmaker.App
import com.example.playlistmaker.domain.settings.api.SettingsRepository

class SettingsRepositoryImpl(private val context: Context) : SettingsRepository {

    override fun isDarkThemeOn(): Boolean {
        return (context as App).isDarkTheme
    }

    override fun switchTheme(isDarkThemeEnabled: Boolean) {
        (context as App).switchTheme(isDarkThemeEnabled)
    }
}