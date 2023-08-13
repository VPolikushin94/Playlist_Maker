package com.example.playlistmaker.ui.search.models

sealed interface SearchScreenState {
    object Loading : SearchScreenState
    data class Content(val isEmpty: Boolean) : SearchScreenState
    object Error : SearchScreenState
}
