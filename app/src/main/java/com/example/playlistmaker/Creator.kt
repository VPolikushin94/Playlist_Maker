package com.example.playlistmaker

import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.repository.AudioPlayerRepositoryImpl
import com.example.playlistmaker.data.repository.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.repository.TrackSearchRepositoryImpl
import com.example.playlistmaker.domain.impl.AudioPlayerInteractorImpl
import com.example.playlistmaker.domain.impl.SearchInteractorImpl
import com.example.playlistmaker.util.SharedPrefsManager


object Creator {
    private val audioPlayerRepository = AudioPlayerRepositoryImpl()
    val audioPlayerInteractor = AudioPlayerInteractorImpl(audioPlayerRepository)

    private val retrofitNetworkClient = RetrofitNetworkClient()

    private val sharedPrefs = SharedPrefsManager.searchInstance ?: throw RuntimeException("Search shared prefs did not init")
    private val searchHistoryRepository = SearchHistoryRepositoryImpl(sharedPrefs)
    private val trackSearchRepository = TrackSearchRepositoryImpl(retrofitNetworkClient)
    val searchInteractor = SearchInteractorImpl(searchHistoryRepository, trackSearchRepository)
}