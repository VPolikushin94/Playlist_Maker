package com.example.playlistmaker.ui.search.models

import com.example.playlistmaker.domain.search.models.Track

sealed interface SearchScreenState {
    object Loading : SearchScreenState
    data class Content(val isEmpty: Boolean, val trackList: List<Track>) : SearchScreenState
    data class HistoryContent(val trackList: List<Track>) : SearchScreenState
    object Error : SearchScreenState
}
