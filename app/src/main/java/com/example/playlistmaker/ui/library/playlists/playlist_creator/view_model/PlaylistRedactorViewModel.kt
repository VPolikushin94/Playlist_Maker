package com.example.playlistmaker.ui.library.playlists.playlist_creator.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.library.playlists.api.PlaylistsInteractor
import com.example.playlistmaker.domain.library.playlists.models.Playlist
import kotlinx.coroutines.launch

class PlaylistRedactorViewModel(private val playlistsInteractor: PlaylistsInteractor) :
    PlaylistCreatorViewModel(
        playlistsInteractor
    ) {

    private val _isExit = MutableLiveData<Boolean>()
    val isExit: LiveData<Boolean> = _isExit

    fun updatePlaylist(playlistId: Int?, name: String, imgName: String, description: String) {
        val playlist = Playlist(
            playlistId = playlistId,
            name = name,
            imgName = imgName,
            description = description
        )

        viewModelScope.launch {
            playlistsInteractor.addPlaylist(playlist)
            _isExit.postValue(true)
        }

    }
}