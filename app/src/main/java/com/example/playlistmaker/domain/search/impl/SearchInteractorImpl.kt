package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.domain.search.api.SearchHistoryRepository
import com.example.playlistmaker.domain.search.api.SearchInteractor
import com.example.playlistmaker.domain.search.api.TrackSearchRepository
import com.example.playlistmaker.domain.search.api.TracksConsumer
import com.example.playlistmaker.domain.search.models.ConsumerData
import com.example.playlistmaker.domain.search.models.Resource
import com.example.playlistmaker.domain.search.models.Track

class SearchInteractorImpl(
    private val searchHistoryRepository: SearchHistoryRepository,
    private val trackSearchRepository: TrackSearchRepository
) : SearchInteractor {

    override fun getHistoryTrackList(): List<Track> {
        return searchHistoryRepository.getHistoryTrackList()
    }

    override fun addTrackToHistory(track: Track) {
        searchHistoryRepository.addTrackToHistory(track)
    }

    override fun clearHistoryTrackList() {
        searchHistoryRepository.clearHistoryTrackList()
    }

    override fun searchTrack(
        expression: String,
        tracksConsumer: TracksConsumer
    ) {
        val t = Thread {
            when(val resource = trackSearchRepository.searchTrack(expression)) {
                is Resource.Success -> tracksConsumer.consume(ConsumerData.Success(resource.data))
                is Resource.Error -> tracksConsumer.consume(ConsumerData.Error(resource.message))
            }
        }
        t.start()
    }

}