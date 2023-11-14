package com.example.playlistmaker.ui.library.playlists.playlist_watcher.models

import com.example.playlistmaker.domain.library.playlists.models.PlaylistAndTracks

sealed interface PlaylistWatcherScreenState {

    object Loading : PlaylistWatcherScreenState

    class Content(val playlistAndTracks: PlaylistAndTracks) : PlaylistWatcherScreenState

    object Close : PlaylistWatcherScreenState
}