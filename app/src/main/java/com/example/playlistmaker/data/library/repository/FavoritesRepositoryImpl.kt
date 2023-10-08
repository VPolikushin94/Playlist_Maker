package com.example.playlistmaker.data.library.repository

import com.example.playlistmaker.data.library.db.AppDatabase
import com.example.playlistmaker.data.library.db.TrackDbConvertor
import com.example.playlistmaker.domain.library.api.FavoritesRepository
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoritesRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor
) : FavoritesRepository {
    override suspend fun addTrack(track: Track) {
        appDatabase.getTrackDao().insertTrack(trackDbConvertor.map(track))
    }

    override suspend fun deleteTrack(track: Track) {
        appDatabase.getTrackDao().deleteTrack(trackDbConvertor.map(track))
    }

    override fun getTrackList(): Flow<List<Track>> = flow {
        val trackEntityList = appDatabase.getTrackDao().getTrackList()
        val trackList = trackEntityList.map { trackDbConvertor.map(it) }
        emit(trackList)
    }
}