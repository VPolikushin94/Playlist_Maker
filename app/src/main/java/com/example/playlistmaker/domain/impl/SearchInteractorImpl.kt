package com.example.playlistmaker.domain.impl

import android.util.Log
import com.example.playlistmaker.domain.api.search.SearchHistoryRepository
import com.example.playlistmaker.domain.api.search.SearchInteractor
import com.example.playlistmaker.domain.api.search.TrackSearchRepository
import com.example.playlistmaker.domain.models.Track

class SearchInteractorImpl(
    private val searchHistoryRepository: SearchHistoryRepository,
    private val trackSearchRepository: TrackSearchRepository
) : SearchInteractor {

    override fun getHistoryTrackList(): ArrayList<Track> {
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
        tracksConsumer: SearchInteractor.TracksConsumer
    ) {
        val t = Thread {
            tracksConsumer.consume(trackSearchRepository.searchTrack(expression))
        }
        t.start()
    }

    override fun initSharedPref(callbackOnSharedPrefChange: () -> Unit) {
        searchHistoryRepository.initSharedPref(callbackOnSharedPrefChange)
    }
}