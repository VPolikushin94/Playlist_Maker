package com.example.playlistmaker.ui.player.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.library.favorites.api.FavoritesInteractor
import com.example.playlistmaker.domain.library.playlists.api.PlaylistsInteractor
import com.example.playlistmaker.domain.library.playlists.models.AddTrackState
import com.example.playlistmaker.domain.library.playlists.models.Playlist
import com.example.playlistmaker.domain.player.api.AudioPlayerInteractor
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.ui.player.models.AudioPlayerState
import com.example.playlistmaker.ui.player.models.BottomSheetContentState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AudioPlayerViewModel(
    private val audioPlayerInteractor: AudioPlayerInteractor,
    private val favoritesInteractor: FavoritesInteractor,
    private val playlistsInteractor: PlaylistsInteractor
) : ViewModel() {

    private val _playerState = MutableLiveData<AudioPlayerState>(AudioPlayerState.Default)
    val playerState: LiveData<AudioPlayerState> = _playerState

    private val _favoriteBtnState = MutableLiveData(false)
    val favoriteBtnState: LiveData<Boolean> = _favoriteBtnState

    private val _bottomSheetContentState = MutableLiveData<BottomSheetContentState>()
    val bottomSheetContentState: LiveData<BottomSheetContentState> = _bottomSheetContentState

    private var timerJob: Job? = null

    lateinit var track: Track

    var isFragmentCreated = false

    fun onFavoriteClicked(track: Track) {
        viewModelScope.launch {
            track.isFavorite = !track.isFavorite
            if(track.isFavorite) {
                favoritesInteractor.addTrack(track)
            } else {
                favoritesInteractor.deleteTrack(track)
            }
            _favoriteBtnState.postValue(track.isFavorite)
        }

    }

    fun prepare(track: Track) {
        _favoriteBtnState.value = track.isFavorite

        audioPlayerInteractor.prepare(
            track.previewUrl,
            callbackOnPrepared = {
                _playerState.value = AudioPlayerState.Prepared
            },
            callbackOnCompletion = {
                timerJob?.cancel()
                _playerState.value = AudioPlayerState.Prepared
            }
        )
    }

    fun addTrackToPlaylist(playlist: Playlist, track: Track) {
        _bottomSheetContentState.value = BottomSheetContentState.Loading
        viewModelScope.launch {
            val result = playlistsInteractor.addTrackToPlaylist(playlist, track)
            if(result == AddTrackState.Success) {
                _bottomSheetContentState.postValue(BottomSheetContentState.AddTrackState(false, playlist.name))
            } else {
                _bottomSheetContentState.postValue(BottomSheetContentState.AddTrackState(true, playlist.name))
            }

        }
    }

    private fun start() {
        audioPlayerInteractor.start()
        startTimer()
    }

    fun pause() {
        if (_playerState.value is AudioPlayerState.Playing) { // т.к. метод вызывается из активити в onPause() и если плеер еще не был запущен, то выдается ошибка
            audioPlayerInteractor.pause()
        }
        timerJob?.cancel()
        _playerState.value = AudioPlayerState.Paused(audioPlayerInteractor.getTrackCurrentTime())
    }

    fun playbackControl() {
        when (_playerState.value) {
            is AudioPlayerState.Paused, AudioPlayerState.Prepared -> start()
            is AudioPlayerState.Playing -> pause()
            else -> throw RuntimeException("Media Player is not prepared")
        }
    }

    private fun startTimer() {
        _playerState.value = AudioPlayerState.Playing(audioPlayerInteractor.getTrackCurrentTime())
        timerJob = viewModelScope.launch {
            while (_playerState.value is AudioPlayerState.Playing) {
                _playerState.postValue(AudioPlayerState.Playing(audioPlayerInteractor.getTrackCurrentTime()))
                delay(UPDATE_TIMER_DELAY_MILLIS)
            }
        }
    }

    fun getPlaylists() {
        _bottomSheetContentState.value = BottomSheetContentState.Loading

        viewModelScope.launch {
            playlistsInteractor.getPlaylists()
                .collect {
                    _bottomSheetContentState.postValue(BottomSheetContentState.Content(it))
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayerInteractor.reset()
    }

    companion object {
        private const val UPDATE_TIMER_DELAY_MILLIS = 300L
    }
}
