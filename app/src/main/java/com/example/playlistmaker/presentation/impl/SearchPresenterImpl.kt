package com.example.playlistmaker.presentation.impl

import android.os.Handler
import android.os.Looper
import com.example.playlistmaker.domain.api.search.SearchInteractor
import com.example.playlistmaker.domain.models.ResponseResult
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.api.search.SearchPresenter
import com.example.playlistmaker.presentation.api.search.SearchView
import com.example.playlistmaker.presentation.models.SearchPlaceholderState

class SearchPresenterImpl(
    private val searchInteractor: SearchInteractor,
    private val searchView: SearchView
) : SearchPresenter {
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    private val searchRunnable = Runnable { searchTrack() }

    private var searchedText: String = ""

    override var searchText: String =""
    override var trackList = ArrayList<Track>()

    init {
        searchInteractor.initSharedPref(
            callbackOnSharedPrefChange = {
                searchView.updateHistoryTrackList()
            }
        )
    }

    override fun getHistoryTrackList(): ArrayList<Track> {
        return searchInteractor.getHistoryTrackList()
    }

    override fun addTrackToHistory(track: Track) {
        searchInteractor.addTrackToHistory(track)
    }

    override fun clearHistoryTrackList() {
        searchInteractor.clearHistoryTrackList()
    }

    override fun searchTrack() {
        handler.removeCallbacks(searchRunnable)
        if (searchedText != searchText) {
            if (searchText.isNotEmpty()) {
                searchView.showProgressBar(true)
                searchInteractor.searchTrack(
                    searchText,
                    object : SearchInteractor.TracksConsumer {
                        override fun consume(responseResult: ResponseResult<List<Track>>) {
                            handler.post {

                                searchView.showProgressBar(false)
                                trackList.clear()
                                when (responseResult) {
                                    is ResponseResult.Success -> {
                                        searchedText = searchText
                                        trackList.addAll(responseResult.data)
                                        searchView.showSearchResult(
                                            SearchPlaceholderState.PLACEHOLDER_HIDDEN,
                                            trackList
                                        )
                                    }
                                    is ResponseResult.NothingFound -> searchView.showSearchResult(
                                        SearchPlaceholderState.PLACEHOLDER_NOTHING_FOUND,
                                        trackList
                                    )
                                    else -> searchView.showSearchResult(
                                        SearchPlaceholderState.PLACEHOLDER_BAD_CONNECTION,
                                        trackList
                                    )
                                }
                            }
                        }
                    }
                )
            }
        }
    }

    override fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY_MILLIS)
        }
        return current
    }

    override fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY_MILLIS)
    }


    companion object {
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
        private const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L
    }
}