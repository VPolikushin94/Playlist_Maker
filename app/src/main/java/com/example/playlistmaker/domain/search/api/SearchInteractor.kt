package com.example.playlistmaker.domain.search.api

import com.example.playlistmaker.domain.search.models.Track

interface SearchInteractor {

    fun getHistoryTrackList(): List<Track>

    fun addTrackToHistory(track: Track)

    fun clearHistoryTrackList()

    fun searchTrack(expression: String, tracksConsumer: TracksConsumer)
}