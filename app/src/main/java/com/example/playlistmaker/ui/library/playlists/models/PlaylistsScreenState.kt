package com.example.playlistmaker.ui.library.playlists.models

import com.example.playlistmaker.domain.library.playlists.models.Playlist

sealed interface PlaylistsScreenState {

    object Loading : PlaylistsScreenState

    class Content(val playlists: List<Playlist>) : PlaylistsScreenState
}