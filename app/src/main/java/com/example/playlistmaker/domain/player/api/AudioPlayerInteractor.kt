package com.example.playlistmaker.domain.player.api

interface AudioPlayerInteractor {

    fun getTrackCurrentTime(): String

    fun release()

    fun prepare(url: String, callbackOnPrepared: () -> Unit, callbackOnCompletion: () -> Unit)

    fun start()

    fun pause()
}