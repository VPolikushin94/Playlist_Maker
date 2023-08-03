package com.example.playlistmaker.domain.api.player

interface AudioPlayerRepository {

    fun getTrackCurrentTime(): String

    fun prepare(url: String, callbackOnPrepared: () -> Unit, callbackOnCompletion: () -> Unit)

    fun release()

    fun start()

    fun pause()
}