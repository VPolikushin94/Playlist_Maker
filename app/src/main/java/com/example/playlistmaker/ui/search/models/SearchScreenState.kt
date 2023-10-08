package com.example.playlistmaker.ui.search.models

sealed interface SearchScreenState {
    object Loading : SearchScreenState
    data class Content(val isEmpty: Boolean) : SearchScreenState
    object HistoryContent : SearchScreenState
    object Error : SearchScreenState
}
