package com.example.playlistmaker.domain.sharing.impl

import com.example.playlistmaker.domain.sharing.api.SharingInteractor
import com.example.playlistmaker.domain.sharing.api.SharingRepository

class SharingInteractorImpl(private val sharingRepository: SharingRepository) : SharingInteractor {

    override fun shareApp(): Boolean {
        return sharingRepository.shareApp()
    }

    override fun openLicence(): Boolean {
        return sharingRepository.openLicence()
    }

    override fun openSupport(): Boolean {
        return sharingRepository.openSupport()
    }
}