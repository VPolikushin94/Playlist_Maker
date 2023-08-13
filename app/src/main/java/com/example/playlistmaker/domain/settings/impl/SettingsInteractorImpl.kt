package com.example.playlistmaker.domain.settings.impl

import com.example.playlistmaker.domain.settings.api.SettingsInteractor
import com.example.playlistmaker.domain.settings.api.SettingsRepository

class SettingsInteractorImpl(private val settingsRepository: SettingsRepository) : SettingsInteractor{

    override fun isDarkThemeOn(): Boolean {
        return settingsRepository.isDarkThemeOn()
    }

    override fun switchTheme(isDarkThemeEnabled: Boolean) {
        settingsRepository.switchTheme(isDarkThemeEnabled)
    }
}