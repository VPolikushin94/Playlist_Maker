package com.example.playlistmaker.ui.library.playlists.playlist_creator.models

sealed interface PlaylistCreatingState {
    object Creating: PlaylistCreatingState
    object Created: PlaylistCreatingState
}