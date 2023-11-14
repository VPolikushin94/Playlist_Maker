package com.example.playlistmaker.domain.sharing.api

import com.example.playlistmaker.domain.library.playlists.models.PlaylistAndTracks

interface SharingRepository {

    fun shareApp(): Boolean

    fun openLicence(): Boolean

    fun openSupport(): Boolean

    fun sharePlaylist(playlistAndTracks: PlaylistAndTracks): Boolean
}