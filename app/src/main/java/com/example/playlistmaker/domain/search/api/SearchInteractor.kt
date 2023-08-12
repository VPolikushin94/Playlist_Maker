package com.example.playlistmaker.domain.search.api

import com.example.playlistmaker.domain.search.models.ConsumerData
import com.example.playlistmaker.domain.search.models.Track

interface SearchInteractor {

    fun getHistoryTrackList(): ArrayList<Track>

    fun addTrackToHistory(track: Track)

    fun clearHistoryTrackList()

    fun searchTrack(expression: String, tracksConsumer: TracksConsumer)

    interface TracksConsumer {
        fun consume(consumerData: ConsumerData<List<Track>>)
    }
}