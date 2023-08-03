package com.example.playlistmaker.presentation.impl

import android.os.Handler
import android.os.Looper
import com.example.playlistmaker.presentation.models.AudioPlayerState
import com.example.playlistmaker.util.DateTimeUtil
import com.example.playlistmaker.domain.api.player.AudioPlayerInteractor
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.api.player.AudioPlayerPresenter
import com.example.playlistmaker.presentation.api.player.AudioPlayerView

class AudioPlayerPresenterImpl(
    val track: Track,
    val audioPlayerInteractor: AudioPlayerInteractor,
    val audioPlayerView: AudioPlayerView
) : AudioPlayerPresenter {

    override var playerState: AudioPlayerState = AudioPlayerState.DEFAULT

    private val handler = Handler(Looper.getMainLooper())

    private val updateTimerTask = object : Runnable {
        override fun run() {
            if (playerState == AudioPlayerState.PLAYING) {
                audioPlayerView.showCurrentTrackTime(audioPlayerInteractor.getTrackCurrentTime())
                handler.postDelayed(this, UPDATE_TIMER_DELAY_MILLIS)
            }
        }
    }

    init {
        audioPlayerView.setButtonPlayerPlayEnabled(false)
        audioPlayerInteractor.prepare(
            track.previewUrl,
            callbackOnPrepared = {
                playerState = AudioPlayerState.PREPARED
                audioPlayerView.setButtonPlayerPlayEnabled(true)
            },
            callbackOnCompletion = {
                playerState = AudioPlayerState.PREPARED
                audioPlayerView.showPlayButtonState(true)
                audioPlayerView.showCurrentTrackTime(DateTimeUtil.getFormatTime(TIME_START))
            }
        )
    }


    override fun release() {
        audioPlayerInteractor.release()
    }

    override fun start() {
        playerState = AudioPlayerState.PLAYING
        audioPlayerInteractor.start()
        startTimer()
        audioPlayerView.showPlayButtonState(false)
    }

    override fun pause() {
        if (playerState == AudioPlayerState.PLAYING) { // т.к. метод вызывается из активити в onPause() и если плеер еще не был запущен, то выдается ошибка
            audioPlayerInteractor.pause()
        }
        playerState = AudioPlayerState.PAUSED
        handler.removeCallbacks(updateTimerTask)
        audioPlayerView.showPlayButtonState(true)
    }

    override fun playbackControl() {
        when (playerState) {
            AudioPlayerState.PAUSED, AudioPlayerState.PREPARED -> start()
            AudioPlayerState.PLAYING -> pause()
            else -> throw RuntimeException("Media Player is not prepared")
        }
    }

    private fun startTimer() {
        handler.post(updateTimerTask)
    }

    companion object {

        private const val UPDATE_TIMER_DELAY_MILLIS = 300L

        private const val TIME_START = 0
    }
}