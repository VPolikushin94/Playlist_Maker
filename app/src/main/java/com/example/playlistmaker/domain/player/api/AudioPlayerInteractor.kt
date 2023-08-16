package com.example.playlistmaker.domain.player.api

interface AudioPlayerInteractor {

    fun getTrackCurrentTime(): String

    fun reset()

    fun prepare(url: String, callbackOnPrepared: () -> Unit, callbackOnCompletion: () -> Unit)

    fun start()

    fun pause()
}