package com.example.playlistmaker.domain.sharing.impl

import com.example.playlistmaker.domain.sharing.api.SharingInteractor
import com.example.playlistmaker.domain.sharing.api.SharingRepository

class SharingInteractorImpl(private val sharingRepository: SharingRepository) : SharingInteractor {

    override fun shareApp() {
        sharingRepository.shareApp()
    }

    override fun openLicence() {
        sharingRepository.openLicence()
    }

    override fun openSupport() {
        sharingRepository.openSupport()
    }
}