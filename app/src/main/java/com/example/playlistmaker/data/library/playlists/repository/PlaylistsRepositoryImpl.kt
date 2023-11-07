package com.example.playlistmaker.data.library.playlists.repository

import com.example.playlistmaker.data.AppDatabase
import com.example.playlistmaker.data.library.playlists.db.convertor.PlaylistDbConverter
import com.example.playlistmaker.data.library.playlists.db.convertor.PlaylistTrackDbConvertor
import com.example.playlistmaker.domain.library.playlists.api.PlaylistsRepository
import com.example.playlistmaker.domain.library.playlists.models.AddTrackState
import com.example.playlistmaker.domain.library.playlists.models.Playlist
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistsRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistDbConverter: PlaylistDbConverter,
    private val playlistTrackDbConvertor: PlaylistTrackDbConvertor
): PlaylistsRepository {

    override suspend fun addPlaylist(playlist: Playlist) {
        appDatabase.getPlaylistDao().insertPlaylist(playlistDbConverter.map(playlist))
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        appDatabase.getPlaylistDao().deletePlaylist(playlistDbConverter.map(playlist))
    }

    override suspend fun addTrackToPlaylist(playlist: Playlist, track: Track): AddTrackState {
        val result = appDatabase.getPlaylistTrackDao().insertTrackToPlaylist(
            playlistDbConverter.map(playlist),
            playlistTrackDbConvertor.map(track)
        ).toInt()
        return if (result == -1) {
            AddTrackState.AlreadyAdded
        } else {
            AddTrackState.Success
        }
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return appDatabase.getPlaylistDao().getPlaylistsWithTracks().map { playlistsWithTracks ->
            playlistsWithTracks.map { playlistDbConverter.map(it) }
        }
    }
}