package com.example.playlistmaker.presentation.api.search

import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.models.SearchPlaceholderState

interface SearchView {

    fun showProgressBar(isVisible: Boolean)

    fun showSearchResult(placeholderState: SearchPlaceholderState, trackList: List<Track>)

    fun updateHistoryTrackList()
}