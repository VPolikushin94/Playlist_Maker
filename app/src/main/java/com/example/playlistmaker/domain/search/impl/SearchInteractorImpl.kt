package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.domain.search.api.SearchHistoryRepository
import com.example.playlistmaker.domain.search.api.SearchInteractor
import com.example.playlistmaker.domain.search.api.TrackSearchRepository
import com.example.playlistmaker.domain.search.models.Resource
import com.example.playlistmaker.domain.search.models.SearchedData
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchInteractorImpl(
    private val searchHistoryRepository: SearchHistoryRepository,
    private val trackSearchRepository: TrackSearchRepository,
) : SearchInteractor {

    override fun getHistoryTrackList(): Flow<List<Track>> {
        return searchHistoryRepository.getHistoryTrackList()
    }

    override fun addTrackToHistory(track: Track) {
        searchHistoryRepository.addTrackToHistory(track)
    }

    override fun clearHistoryTrackList() {
        searchHistoryRepository.clearHistoryTrackList()
    }

    override fun searchTrack(expression: String): Flow<SearchedData<List<Track>>> {
        return trackSearchRepository.searchTrack(expression).map { result ->
            when (result) {
                is Resource.Success -> SearchedData.Success(result.data)
                is Resource.Error -> SearchedData.Error(result.message)
            }
        }
    }

    override fun updateSearchTrackList(): Flow<List<Track>> {
        return trackSearchRepository.updateSearchTrackList()
    }

}