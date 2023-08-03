package com.example.playlistmaker.domain.api.search

import com.example.playlistmaker.domain.models.Track

interface SearchHistoryRepository {

    fun getHistoryTrackList(): ArrayList<Track>

    fun addTrackToHistory(track: Track)

    fun clearHistoryTrackList()

    fun initSharedPref(callbackOnSharedPrefChange:() -> Unit)
}