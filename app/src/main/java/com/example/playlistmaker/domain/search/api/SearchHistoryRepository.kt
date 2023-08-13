package com.example.playlistmaker.domain.search.api

import com.example.playlistmaker.domain.search.models.Track

interface SearchHistoryRepository {

    fun getHistoryTrackList(): List<Track>

    fun addTrackToHistory(track: Track)

    fun clearHistoryTrackList()

}