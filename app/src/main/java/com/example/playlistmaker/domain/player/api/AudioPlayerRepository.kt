package com.example.playlistmaker.domain.player.api

interface AudioPlayerRepository {

    fun getTrackCurrentTime(): String

    fun prepare(url: String, callbackOnPrepared: () -> Unit, callbackOnCompletion: () -> Unit)

    fun reset()

    fun start()

    fun pause()
}