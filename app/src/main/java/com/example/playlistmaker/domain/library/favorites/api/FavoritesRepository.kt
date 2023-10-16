package com.example.playlistmaker.domain.library.favorites.api

import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {

    suspend fun addTrack(track: Track)

    suspend fun deleteTrack(track: Track)

    fun getTrackList(): Flow<List<Track>>
}