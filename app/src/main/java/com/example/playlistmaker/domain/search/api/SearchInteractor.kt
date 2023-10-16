package com.example.playlistmaker.domain.search.api

import com.example.playlistmaker.domain.search.models.SearchedData
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

interface SearchInteractor {

    fun getHistoryTrackList(): Flow<List<Track>>

    fun addTrackToHistory(track: Track)

    fun clearHistoryTrackList()

    fun searchTrack(expression: String): Flow<SearchedData<List<Track>>>

    fun updateSearchTrackList(): Flow<List<Track>>
}