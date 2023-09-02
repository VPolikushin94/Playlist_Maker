package com.example.playlistmaker.ui.settings.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.settings.api.SettingsInteractor
import com.example.playlistmaker.domain.sharing.api.SharingInteractor
import com.example.playlistmaker.ui.settings.models.ShareState
import com.example.playlistmaker.ui.settings.models.ShareType

class SettingsViewModel(
    private val settingsInteractor: SettingsInteractor,
    private val sharingInteractor: SharingInteractor
    ) : ViewModel() {

    fun isDarkThemeOn(): Boolean = settingsInteractor.isDarkThemeOn()

    private val _shareState = MutableLiveData<ShareState>()
    val shareState: LiveData<ShareState> = _shareState

    fun switchTheme(isDarkThemeEnabled: Boolean) {
        settingsInteractor.switchTheme(isDarkThemeEnabled)
    }

    fun shareApp() {
        if(sharingInteractor.shareApp()) {
            _shareState.value = ShareState.Success
        } else {
            _shareState.value = ShareState.Error(ShareType.SHARE_APP)
        }
    }

    fun openLicence() {
        if(sharingInteractor.openLicence()) {
            _shareState.value = ShareState.Success
        } else {
            _shareState.value = ShareState.Error(ShareType.OPEN_LICENSE)
        }
    }

    fun openSupport() {
        if(sharingInteractor.openSupport()) {
            _shareState.value = ShareState.Success
        } else {
            _shareState.value = ShareState.Error(ShareType.OPEN_SUPPORT)
        }
    }

}