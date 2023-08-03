package com.example.playlistmaker.data.repository

import android.media.MediaPlayer
import com.example.playlistmaker.util.DateTimeUtil
import com.example.playlistmaker.domain.api.player.AudioPlayerRepository

class AudioPlayerRepositoryImpl : AudioPlayerRepository {

    private lateinit var mediaPlayer: MediaPlayer

    override fun getTrackCurrentTime(): String {
        return DateTimeUtil.getFormatTime(mediaPlayer.currentPosition)
    }

    override fun release() {
        mediaPlayer.release()
    }

    override fun prepare(
        url: String,
        callbackOnPrepared: () -> Unit,
        callbackOnCompletion: () -> Unit
    ) {
        mediaPlayer = MediaPlayer()
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