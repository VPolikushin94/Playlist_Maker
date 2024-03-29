package com.example.playlistmaker.ui.player.models

sealed interface AudioPlayerState {
    object Default : AudioPlayerState
    object Prepared : AudioPlayerState
    data class Playing(val time: String) : AudioPlayerState
    data class Paused(val time: String) : AudioPlayerState
}