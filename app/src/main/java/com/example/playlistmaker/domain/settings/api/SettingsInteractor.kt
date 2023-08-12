package com.example.playlistmaker.domain.settings.api

interface SettingsInteractor {
    fun isDarkThemeOn(): Boolean

    fun switchTheme(isDarkThemeEnabled: Boolean)
}