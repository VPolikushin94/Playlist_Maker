package com.example.playlistmaker.ui.player.models

import com.example.playlistmaker.domain.library.playlists.models.Playlist

sealed interface BottomSheetContentState {

    object Loading : BottomSheetContentState

    class Content(val playlists: List<Playlist>) : BottomSheetContentState

    class AddTrackState(val isAlreadyAdded: Boolean, val playlistName: String) : BottomSheetContentState
}