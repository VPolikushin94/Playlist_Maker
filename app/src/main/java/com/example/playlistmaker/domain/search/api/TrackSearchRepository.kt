package com.example.playlistmaker.domain.search.api

import com.example.playlistmaker.domain.search.models.Resource
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

interface TrackSearchRepository {
    fun searchTrack(expression: String): Flow<Resource<List<Track>>>

    fun updateSearchTrackList(): Flow<List<Track>>
}