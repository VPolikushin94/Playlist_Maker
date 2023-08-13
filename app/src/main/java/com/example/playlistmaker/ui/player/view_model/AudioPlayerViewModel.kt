package com.example.playlistmaker.ui.player.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.Creator
import com.example.playlistmaker.domain.player.api.AudioPlayerInteractor
import com.example.playlistmaker.ui.player.models.AudioPlayerState
import com.example.playlistmaker.domain.search.models.Track

class AudioPlayerViewModel(private val audioPlayerInteractor: AudioPlayerInteractor) : ViewModel() {

    private val _playerState = MutableLiveData<AudioPlayerState>(AudioPlayerState.Default)
    val playerState: LiveData<AudioPlayerState> = _playerState

    private val handler = Handler(Looper.getMainLooper())

    private val updateTimerTask = object : Runnable {
        override fun run() {
            _playerState.value = AudioPlayerState.Playing(audioPlayerInteractor.getTrackCurrentTime())
            handler.postDelayed(this, UPDATE_TIMER_DELAY_MILLIS)
        }
    }
    fun prepare(track: Track) {
        audioPlayerInteractor.prepare(
            track.previewUrl,
            callbackOnPrepared = {
                _playerState.value = AudioPlayerState.Prepared
            },
            callbackOnCompletion = {
                handler.removeCallbacks(updateTimerTask)
                _playerState.value = AudioPlayerState.Prepared
            }
        )
    }

    fun release() {
        audioPlayerInteractor.release()
    }

    private fun start() {
        audioPlayerInteractor.start()
        startTimer()
    }

    fun pause() {
        if (_playerState.value is AudioPlayerState.Playing) { // т.к. метод вызывается из активити в onPause() и если плеер еще не был запущен, то выдается ошибка
            audioPlayerInteractor.pause()
        }
        _playerState.value = AudioPlayerState.Paused
        handler.removeCallbacks(updateTimerTask)
    }

    fun playbackControl() {
        when (_playerState.value) {
            is AudioPlayerState.Paused, AudioPlayerState.Prepared -> start()
            is AudioPlayerState.Playing -> pause()
            else -> throw RuntimeException("Media Player is not prepared")
        }
    }

    private fun startTimer() {
        handler.post(updateTimerTask)
    }

    companion object {
        private const val UPDATE_TIMER_DELAY_MILLIS = 300L

        fun getAudioPlayerFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                AudioPlayerViewModel(Creator.audioPlayerInteractor)
            }
        }
    }
}