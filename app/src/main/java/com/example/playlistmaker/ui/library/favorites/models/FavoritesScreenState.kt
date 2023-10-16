package com.example.playlistmaker.ui.library.favorites.models

sealed interface FavoritesScreenState{
    object Loading : FavoritesScreenState
    data class Content(val isEmpty: Boolean) : FavoritesScreenState
}