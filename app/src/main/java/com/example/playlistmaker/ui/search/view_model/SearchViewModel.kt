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
            searchTrack(false)
        }

    private var searchedText: String = ""

    var searchText: String = ""
    var trackList = ArrayList<Track>()
    var historyTrackList = ArrayList<Track>()

    init {
        getHistoryTrackList()
    }

    private fun updateHistoryList() {
        viewModelScope.launch {
            historyTrackList.clear()
            searchInteractor.getHistoryTrackList().collect{
                historyTrackList.addAll(it)
            }
        }
    }

    fun getHistoryTrackList(){
        _searchScreenState.value = SearchScreenState.Loading
        viewModelScope.launch {
            historyTrackList.clear()
            searchInteractor.getHistoryTrackList().collect{
                if(it.isEmpty()) {
                    _searchScreenState.postValue(SearchScreenState.Content(false))
                } else {
                    historyTrackList.addAll(it)
                    _searchScreenState.postValue(SearchScreenState.HistoryContent)
                }
            }
        }
    }

    fun addTrackToHistory(track: Track) {
        searchInteractor.addTrackToHistory(track)
        updateHistoryList()
    }

    fun clearHistoryTrackList() {
        searchInteractor.clearHistoryTrackList()
        historyTrackList.clear()
    }

    fun updateSearchTrackList() {
        _searchScreenState.value = SearchScreenState.Loading
        viewModelScope.launch {
            searchInteractor.updateSearchTrackList()
                .collect{
                    updateResult(it)
                }
        }
    }

    private fun updateResult(updatedData: List<Track>) {
        trackList.clear()
        trackList.addAll(updatedData)
        _searchScreenState.postValue(SearchScreenState.Content(false))
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