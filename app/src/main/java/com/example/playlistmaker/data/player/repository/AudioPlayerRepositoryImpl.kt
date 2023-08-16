package com.example.playlistmaker.data.player.repository

import android.media.MediaPlayer
import com.example.playlistmaker.util.DateTimeUtil
import com.example.playlistmaker.domain.player.api.AudioPlayerRepository

class AudioPlayerRepositoryImpl(private val mediaPlayer: MediaPlayer) : AudioPlayerRepository {

    override fun getTrackCurrentTime(): String {
        return DateTimeUtil.getFormatTime(mediaPlayer.currentPosition)
    }

    override fun reset() {
        mediaPlayer.reset()
    }

    override fun prepare(
        url: String,
        callbackOnPrepared: () -> Unit,
        callbackOnCompletion: () -> Unit
    ) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            callbackOnPrepared.invoke()
        }
        mediaPlayer.setOnCompletionListener {
            callbackOnCompletion.invoke()
        }
    }

    override fun start() {
        mediaPlayer.start()
    }

    override fun pause() {
        mediaPlayer.pause()
    }
}