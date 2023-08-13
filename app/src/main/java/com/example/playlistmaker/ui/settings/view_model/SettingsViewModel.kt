package com.example.playlistmaker.ui.settings.view_model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.Creator
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

    companion object {
        fun getSettingsViewModel(): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val app = this[APPLICATION_KEY] as Application
                    SettingsViewModel(
                        Creator.provideSettingsInteractor(app),
                        Creator.provideSharingInteractor(app)
                    )
                }
            }
    }
}