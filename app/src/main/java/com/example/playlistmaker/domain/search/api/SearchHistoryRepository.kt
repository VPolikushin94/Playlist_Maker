package com.example.playlistmaker.domain.search.api

import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

interface SearchHistoryRepository {

    fun getHistoryTrackList(): Flow<List<Track>>

    fun addTrackToHistory(track: Track)

    fun clearHistoryTrackList()

}