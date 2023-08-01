package com.example.playlistmaker.presentation.api.search

import com.example.playlistmaker.domain.models.Track

interface SearchPresenter {

    var trackList: ArrayList<Track>
    var searchText: String

    fun getHistoryTrackList(): ArrayList<Track>

    fun addTrackToHistory(track: Track)

    fun clearHistoryTrackList()

    fun searchTrack()

    fun clickDebounce(): Boolean

    fun searchDebounce()
}