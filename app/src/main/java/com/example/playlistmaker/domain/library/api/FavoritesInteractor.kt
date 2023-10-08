package com.example.playlistmaker.domain.library.api

import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesInteractor {
    suspend fun addTrack(track: Track)

    suspend fun deleteTrack(track: Track)

    fun getTrackList(): Flow<List<Track>>
}