package com.example.playlistmaker.ui.search.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.search.api.SearchInteractor
import com.example.playlistmaker.domain.search.models.SearchedData
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.ui.search.models.SearchScreenState
import com.example.playlistmaker.util.debounce
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(private val searchInteractor: SearchInteractor) : ViewModel() {

    private var isClickAllowed = true

    private val _searchScreenState = MutableLiveData<SearchScreenState>()
    val searchScreenState: LiveData<SearchScreenState> = _searchScreenState

    private val searchDebounce =
        debounce<String>(SEARCH_DEBOUNCE_DELAY_MILLIS, viewModelScope, true) {
            searchTrack(true)
        }

    private var searchedText: String = ""

    var searchText: String = ""
    var trackList = ArrayList<Track>()

    fun getHistoryTrackList(): ArrayList<Track> {
        return searchInteractor.getHistoryTrackList() as ArrayList<Track>
    }

    fun addTrackToHistory(track: Track) {
        searchInteractor.addTrackToHistory(track)
    }

    fun clearHistoryTrackList() {
        searchInteractor.clearHistoryTrackList()
    }

    fun searchTrack(isBtnClicked: Boolean) {
        if (isBtnClicked && (searchedText == searchText)) {
            return
        }

        if (searchText.isNotEmpty()) {
            _searchScreenState.value = SearchScreenState.Loading

            viewModelScope.launch {
                searchInteractor.searchTrack(searchText)
                    .collect {
                        processResult(it)
                    }
            }
        }
    }

    private fun processResult(searchedData: SearchedData<List<Track>>) {
        trackList.clear()
        when (searchedData) {
            is SearchedData.Success -> {
                if (searchedData.data.isEmpty()) {
                    _searchScreenState.postValue(SearchScreenState.Content(true))
                } else {
                    searchedText = searchText
                    trackList.addAll(searchedData.data)
                    _searchScreenState.postValue(SearchScreenState.Content(false))
                }
            }

            is SearchedData.Error -> {
                _searchScreenState.postValue(SearchScreenState.Error)
            }
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

    fun searchDebounce() {
        searchDebounce(searchedText)
    }


    companion object {
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
        private const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L
    }
}