package com.example.playlistmaker.ui.search.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.search.api.SearchInteractor
import com.example.playlistmaker.domain.search.api.TracksConsumer
import com.example.playlistmaker.domain.search.models.ConsumerData
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.ui.search.models.SearchScreenState

class SearchViewModel(private val searchInteractor: SearchInteractor) : ViewModel() {

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    private val _searchScreenState = MutableLiveData<SearchScreenState>()
    val searchScreenState: LiveData<SearchScreenState> = _searchScreenState

    private val searchRunnable = Runnable { searchTrack(false) }

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

        handler.removeCallbacks(searchRunnable)

        if (searchText.isNotEmpty()) {
            _searchScreenState.value = SearchScreenState.Loading
            searchInteractor.searchTrack(
                searchText,
                object : TracksConsumer {
                    override fun consume(consumerData: ConsumerData<List<Track>>) {
                        trackList.clear()
                        when (consumerData) {
                            is ConsumerData.Success -> {
                                if (consumerData.data.isEmpty()) {
                                    _searchScreenState.postValue(SearchScreenState.Content(true))
                                } else {
                                    searchedText = searchText
                                    trackList.addAll(consumerData.data)
                                    _searchScreenState.postValue(SearchScreenState.Content(false))
                                }
                            }
                            is ConsumerData.Error -> {
                                _searchScreenState.postValue(SearchScreenState.Error)
                            }
                        }
                    }
                }
            )
        }
    }

    fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY_MILLIS)
        }
        return current
    }

    fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY_MILLIS)
    }


    companion object {
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
        private const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L
    }
}