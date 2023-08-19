package com.example.playlistmaker.ui.settings.view_model

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.settings.api.SettingsInteractor
import com.example.playlistmaker.domain.sharing.api.SharingInteractor

class SettingsViewModel(
    private val settingsInteractor: SettingsInteractor,
    private val sharingInteractor: SharingInteractor
    ) : ViewModel() {

    fun isDarkThemeOn(): Boolean = settingsInteractor.isDarkThemeOn()

    fun switchTheme(isDarkThemeEnabled: Boolean) {
        settingsInteractor.switchTheme(isDarkThemeEnabled)
    }

    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun openLicence() {
        sharingInteractor.openLicence()
    }

    fun openSupport() {
        sharingInteractor.openSupport()
    }

}