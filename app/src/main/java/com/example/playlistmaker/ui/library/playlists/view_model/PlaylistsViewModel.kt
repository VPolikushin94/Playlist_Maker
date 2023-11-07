package com.example.playlistmaker.ui.library.playlists.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.library.playlists.api.PlaylistsInteractor
import com.example.playlistmaker.ui.library.playlists.models.PlaylistsScreenState
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    private val playlistsInteractor: PlaylistsInteractor
) : ViewModel() {

    private val _playlistsScreenState = MutableLiveData<PlaylistsScreenState>()
    val playlistsScreenState: LiveData<PlaylistsScreenState> = _playlistsScreenState

    fun getPlaylists() {
        _playlistsScreenState.value = PlaylistsScreenState.Loading

        viewModelScope.launch {
            playlistsInteractor.getPlaylists()
                .collect {
                    _playlistsScreenState.postValue(PlaylistsScreenState.Content(it))
                }
        }
    }
}