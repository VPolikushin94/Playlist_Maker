package com.example.playlistmaker.domain.library.playlists.impl

import com.example.playlistmaker.domain.library.playlists.api.PlaylistsInteractor
import com.example.playlistmaker.domain.library.playlists.api.PlaylistsRepository
import com.example.playlistmaker.domain.library.playlists.models.AddTrackState
import com.example.playlistmaker.domain.library.playlists.models.Playlist
import com.example.playlistmaker.domain.library.playlists.models.PlaylistAndTracks
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

class PlaylistsInteractorImpl(private val playlistsRepository: PlaylistsRepository): PlaylistsInteractor {
    override suspend fun addPlaylist(playlist: Playlist) {
        playlistsRepository.addPlaylist(playlist)
    }

    override suspend fun deletePlaylist(playlistAndTracks: PlaylistAndTracks) {
        return playlistsRepository.deletePlaylist(playlistAndTracks)
    }

    override suspend fun addTrackToPlaylist(playlist: Playlist,track: Track): AddTrackState {
        return playlistsRepository.addTrackToPlaylist(playlist, track)
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return playlistsRepository.getPlaylists()
    }

    override fun getPlaylistTracks(playlistId: Int): Flow<List<Track>> {
        return playlistsRepository.getPlaylistTracks(playlistId)
    }

    override suspend fun deleteTrackFromPlaylist(playlist: Playlist, track: Track) {
        playlistsRepository.deleteTrackFromPlaylist(playlist, track)
    }

    override fun getPlaylistAndTracks(playlistId: Int): Flow<PlaylistAndTracks> {
        return playlistsRepository.getPlaylistAndTracks(playlistId)
    }
}