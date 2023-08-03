package com.example.playlistmaker.presentation.api.player

import com.example.playlistmaker.presentation.models.AudioPlayerState

interface AudioPlayerPresenter {

    var playerState: AudioPlayerState

    fun release()

    fun start()

    fun pause()

    fun playbackControl()
}