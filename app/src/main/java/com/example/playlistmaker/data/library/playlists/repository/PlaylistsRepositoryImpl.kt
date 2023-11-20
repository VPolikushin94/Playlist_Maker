package com.example.playlistmaker.data.library.playlists.repository

import com.example.playlistmaker.data.AppDatabase
import com.example.playlistmaker.data.library.playlists.db.convertor.PlaylistDbConverter
import com.example.playlistmaker.data.library.playlists.db.convertor.PlaylistTrackDbConvertor
import com.example.playlistmaker.domain.library.playlists.api.PlaylistsRepository
import com.example.playlistmaker.domain.library.playlists.models.AddTrackState
import com.example.playlistmaker.domain.library.playlists.models.Playlist
import com.example.playlistmaker.domain.library.playlists.models.PlaylistAndTracks
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

class PlaylistsRepositoryImpl(
    appDatabase: AppDatabase,
    private val playlistDbConverter: PlaylistDbConverter,
    private val playlistTrackDbConvertor: PlaylistTrackDbConvertor,
) : PlaylistsRepository {

    private val playlistDao = appDatabase.getPlaylistDao()
    private val playlistTrackDao = appDatabase.getPlaylistTrackDao()

    override suspend fun addPlaylist(playlist: Playlist) {
        if (playlist.playlistId == null) {
            playlistDao.insertPlaylist(playlistDbConverter.map(playlist))
        } else {
            playlistDao.updatePlaylist(playlistDbConverter.map(playlist))
        }
    }

    override suspend fun deletePlaylist(playlistAndTracks: PlaylistAndTracks) {
        playlistAndTracks.tracks.forEach { track ->
            deleteTrackFromPlaylist(playlistAndTracks.playlist, track)
        }
        playlistDao.deletePlaylist(playlistDbConverter.map(playlistAndTracks.playlist))
    }

    override suspend fun addTrackToPlaylist(playlist: Playlist, track: Track): AddTrackState {
        val result = playlistTrackDao.insertTrackToPlaylist(
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
        return playlistDao.getPlaylistsWithTracks().map { playlistsWithTracks ->
            playlistsWithTracks.map { playlistDbConverter.map(it) }
        }
    }

    override fun getPlaylistTracks(playlistId: Int): Flow<List<Track>> {
        return playlistDao.getPlaylistWithTracksById(playlistId)
            .filter { playlistWithTracks ->
                playlistWithTracks != null
            }
            .map { playlistsWithTracks -> playlistsWithTracks!!
                playlistsWithTracks.tracks.map { playlistTrackDbConvertor.map(it) }
            }
    }

    override suspend fun deleteTrackFromPlaylist(playlist: Playlist, track: Track) {
        val trackCount = playlistTrackDao.getTrackCount(track.trackId)
        if (trackCount > 1) {
            playlistTrackDao.deleteTrackCrossRef(
                playlist.playlistId ?: throw RuntimeException("Empty playlistId"),
                track.trackId
            )
        } else {
            playlistTrackDao.deleteTrack(playlistTrackDbConvertor.map(track))
        }
    }

    override fun getPlaylistAndTracks(playlistId: Int): Flow<PlaylistAndTracks> {
        return playlistDao.getPlaylistWithTracksById(playlistId)
            .filter { playlistWithTracks ->
                playlistWithTracks != null
            }
            .map { playlistsWithTracks -> playlistsWithTracks!!
                PlaylistAndTracks(
                    playlistDbConverter.map(playlistsWithTracks),
                    playlistsWithTracks.tracks.map { playlistTrackDbConvertor.map(it) }
                )
            }

    }
}