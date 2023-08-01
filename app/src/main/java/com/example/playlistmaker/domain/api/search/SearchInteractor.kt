package com.example.playlistmaker.domain.api.search

import com.example.playlistmaker.domain.models.ResponseResult
import com.example.playlistmaker.domain.models.Track

interface SearchInteractor {

    fun getHistoryTrackList(): ArrayList<Track>

    fun addTrackToHistory(track: Track)

    fun clearHistoryTrackList()

    fun searchTrack(expression: String, tracksConsumer: TracksConsumer)

    fun initSharedPref(callbackOnSharedPrefChange:() -> Unit)

    interface TracksConsumer {
        fun consume(responseResult: ResponseResult<List<Track>>)
    }
}