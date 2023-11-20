package com.example.playlistmaker.ui.library.favorites.models

import com.example.playlistmaker.domain.search.models.Track

sealed interface FavoritesScreenState{
    object Loading : FavoritesScreenState
    data class Content(val isEmpty: Boolean, val trackList: List<Track>) : FavoritesScreenState
}