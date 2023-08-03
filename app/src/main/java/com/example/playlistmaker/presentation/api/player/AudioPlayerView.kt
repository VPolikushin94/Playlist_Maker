package com.example.playlistmaker.presentation.api.player

interface AudioPlayerView {

    fun showPlayButtonState(isPlayBtn: Boolean)

    fun showCurrentTrackTime(time: String)

    fun setButtonPlayerPlayEnabled(isEnabled: Boolean)
}