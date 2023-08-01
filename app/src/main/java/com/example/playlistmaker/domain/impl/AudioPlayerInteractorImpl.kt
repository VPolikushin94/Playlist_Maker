package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.player.AudioPlayerInteractor
import com.example.playlistmaker.domain.api.player.AudioPlayerRepository

class AudioPlayerInteractorImpl(private val audioPlayerRepository: AudioPlayerRepository) :
    AudioPlayerInteractor {

    override fun getTrackCurrentTime(): String {
        return audioPlayerRepository.getTrackCurrentTime()
    }

    override fun release() {
        audioPlayerRepository.release()
    }

    override fun prepare(
        url: String,
        callbackOnPrepared: () -> Unit,
        callbackOnCompletion: () -> Unit
    ) {
        audioPlayerRepository.prepare(
            url,
            callbackOnPrepared,
            callbackOnCompletion
        )
    }

    override fun start() {
        audioPlayerRepository.start()
    }

    override fun pause() {
        audioPlayerRepository.pause()
    }
}