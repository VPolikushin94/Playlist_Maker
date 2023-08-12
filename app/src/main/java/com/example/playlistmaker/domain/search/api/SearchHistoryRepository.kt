package com.example.playlistmaker.domain.search.api

import com.example.playlistmaker.domain.search.models.Track

interface SearchHistoryRepository {

    fun getHistoryTrackList(): ArrayList<Track>

    fun addTrackToHistory(track: Track)

    fun clearHistoryTrackList()

}