package com.example.playlistmaker.domain.library.playlists.api

import com.example.playlistmaker.domain.library.playlists.models.AddTrackState
import com.example.playlistmaker.domain.library.playlists.models.Playlist
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {

    suspend fun addPlaylist(playlist: Playlist)

    suspend fun deletePlaylist(playlist: Playlist)

    suspend fun addTrackToPlaylist(playlist: Playlist, track: Track): AddTrackState

    fun getPlaylists(): Flow<List<Playlist>>
}