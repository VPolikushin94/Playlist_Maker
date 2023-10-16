package com.example.playlistmaker.ui.library.favorites.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.library.favorites.api.FavoritesInteractor
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.ui.library.favorites.models.FavoritesScreenState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val favoritesInteractor: FavoritesInteractor,
) : ViewModel() {

    private val _favoritesScreenState = MutableLiveData<FavoritesScreenState>()
    val favoritesScreenState: LiveData<FavoritesScreenState> = _favoritesScreenState

    var favoritesList = ArrayList<Track>()

    private var isClickAllowed = true

    fun getFavoritesList() {
        _favoritesScreenState.value = FavoritesScreenState.Loading

        viewModelScope.launch {
            favoritesInteractor.getTrackList()
                .collect {
                    processResult(it)
                }
        }
    }

    private fun processResult(trackList: List<Track>) {
        favoritesList.clear()

        if (trackList.isEmpty()) {
            _favoritesScreenState.postValue(FavoritesScreenState.Content(true))
        } else {
            favoritesList.addAll(trackList)
            _favoritesScreenState.postValue(FavoritesScreenState.Content(false))
        }
    }

    fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewModelScope.launch {
                delay(CLICK_DEBOUNCE_DELAY_MILLIS)
                isClickAllowed = true
            }
        }
        return current
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
    }
}