package com.example.playlistmaker.ui.library.playlists.playlist_watcher.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.library.playlists.api.PlaylistsInteractor
import com.example.playlistmaker.domain.library.playlists.models.Playlist
import com.example.playlistmaker.domain.library.playlists.models.PlaylistAndTracks
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.domain.sharing.api.SharingInteractor
import com.example.playlistmaker.ui.library.playlists.playlist_watcher.models.PlaylistWatcherScreenState
import com.example.playlistmaker.ui.settings.models.ShareState
import com.example.playlistmaker.ui.settings.models.ShareType
import kotlinx.coroutines.launch

class PlaylistWatcherViewModel(
    private val playlistsInteractor: PlaylistsInteractor,
    private val sharingInteractor: SharingInteractor,
) : ViewModel() {

    private val _playlistWatcherScreenState = MutableLiveData<PlaylistWatcherScreenState>()
    val playlistWatcherScreenState: LiveData<PlaylistWatcherScreenState> =
        _playlistWatcherScreenState

    private var _currentPlaylist: PlaylistAndTracks? = null
    val currentPlaylist get() = _currentPlaylist ?: throw RuntimeException("Empty _currentPlaylist")

    private val _shareState = MutableLiveData<ShareState>()
    val shareState: LiveData<ShareState> = _shareState

    fun updatePlayListAndTracks(playlistId: Int) {
        _playlistWatcherScreenState.value = PlaylistWatcherScreenState.Loading
        viewModelScope.launch {
            playlistsInteractor.getPlaylistAndTracks(playlistId).collect {
                _currentPlaylist = it
                _playlistWatcherScreenState.postValue(PlaylistWatcherScreenState.Content(it))
            }
        }
    }

    fun deleteTrack(playlist: Playlist, track: Track) {
        viewModelScope.launch {
            playlistsInteractor.deleteTrackFromPlaylist(playlist, track)
        }
    }

    fun deletePlaylist(playlistAndTracks: PlaylistAndTracks) {
        _playlistWatcherScreenState.value = PlaylistWatcherScreenState.Loading
        viewModelScope.launch {
            playlistsInteractor.deletePlaylist(playlistAndTracks)
            _playlistWatcherScreenState.postValue(PlaylistWatcherScreenState.Close)
        }
    }

    fun sharePlaylist(playlistAndTracks: PlaylistAndTracks) {
        if (sharingInteractor.sharePlaylist(playlistAndTracks)) {
            _shareState.value = ShareState.Success
        } else {
            _shareState.value = ShareState.Error(ShareType.SHARE_PLAYLIST)
        }
    }
}