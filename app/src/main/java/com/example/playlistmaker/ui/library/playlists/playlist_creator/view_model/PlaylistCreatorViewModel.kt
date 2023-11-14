package com.example.playlistmaker.ui.library.playlists.playlist_creator.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.library.playlists.api.PlaylistsInteractor
import com.example.playlistmaker.domain.library.playlists.models.Playlist
import com.example.playlistmaker.ui.library.playlists.playlist_creator.models.PlaylistCreatingState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

open class PlaylistCreatorViewModel(
    private val playlistsInteractor: PlaylistsInteractor
) : ViewModel() {

    private var isClickAllowed = true

    private val _playlistCreatingState = MutableLiveData<PlaylistCreatingState>()
    val playlistCreatingState: LiveData<PlaylistCreatingState> = _playlistCreatingState

    fun addPlaylist(
        playlistId: Int?,
        name: String,
        imgName: String,
        description: String
    ) {
        _playlistCreatingState.value = PlaylistCreatingState.Creating
        val playlist = Playlist(
            playlistId = playlistId,
            name = name,
            imgName = imgName,
            description = description
        )

        viewModelScope.launch {
            playlistsInteractor.addPlaylist(playlist)
            _playlistCreatingState.postValue(PlaylistCreatingState.Created)
        }
    }

    fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewModelScope.launch {
                delay(CLICK_DEBOUNCE_DELAY_MILLIS)
                isClickAllowed = true
            }
        }
        return current
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
    }
}