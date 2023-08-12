package com.example.playlistmaker.domain.settings.api

interface SettingsRepository {
    fun isDarkThemeOn(): Boolean

    fun switchTheme(isDarkThemeEnabled: Boolean)
}