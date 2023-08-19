package com.example.playlistmaker.domain.sharing.api

interface SharingInteractor {

    fun shareApp(): Boolean

    fun openLicence(): Boolean

    fun openSupport(): Boolean

}