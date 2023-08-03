package com.example.playlistmaker.data.repository

import android.content.SharedPreferences
import com.example.playlistmaker.domain.api.search.SearchHistoryRepository
import com.example.playlistmaker.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistoryRepositoryImpl(private val sharedPrefs: SharedPreferences) :
    SearchHistoryRepository {

    private lateinit var sharedPrefsListener: SharedPreferences.OnSharedPreferenceChangeListener
    private var historyTrackList: ArrayList<Track>

    init {
        historyTrackList =
            createTrackListFromJson(sharedPrefs.getString(SEARCH_HISTORY_KEY, "") ?: "")
    }

    override fun getHistoryTrackList(): ArrayList<Track> {
        return historyTrackList
    }

    override fun addTrackToHistory(track: Track) {
        historyTrackList.removeIf {
            it.trackId == track.trackId
        }
        if (historyTrackList.size == LIST_MAX_SIZE) {
            historyTrackList.removeAt(LIST_MAX_SIZE - 1)
        }
        historyTrackList.add(0, track)
        sharedPrefs.edit()
            .putString(SEARCH_HISTORY_KEY, createJsonFromTrackList(historyTrackList))
            .apply()
    }

    override fun clearHistoryTrackList() {
        historyTrackList.clear()
        sharedPrefs.edit()
            .remove(SEARCH_HISTORY_KEY)
            .apply()
    }

    override fun initSharedPref(callbackOnSharedPrefChange: () -> Unit) {
        sharedPrefsListener =
            SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
                if (key == SEARCH_HISTORY_KEY) {
                    callbackOnSharedPrefChange.invoke()
                }
            }
        sharedPrefs.registerOnSharedPreferenceChangeListener(sharedPrefsListener)
    }

    private fun createJsonFromTrackList(trackList: ArrayList<Track>): String {
        return Gson().toJson(trackList)
    }

    private fun createTrackListFromJson(json: String): ArrayList<Track> {
        val type = object : TypeToken<ArrayList<Track>>() {}.type
        return Gson().fromJson(json, type) ?: arrayListOf()
    }

    companion object {
        private const val LIST_MAX_SIZE = 10

        const val SEARCH_HISTORY_KEY = "SEARCH_HISTORY_KEY"
    }
}