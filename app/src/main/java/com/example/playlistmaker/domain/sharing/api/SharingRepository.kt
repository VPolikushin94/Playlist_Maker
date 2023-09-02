package com.example.playlistmaker.domain.sharing.api

interface SharingRepository {

    fun shareApp(): Boolean

    fun openLicence(): Boolean

    fun openSupport(): Boolean
}