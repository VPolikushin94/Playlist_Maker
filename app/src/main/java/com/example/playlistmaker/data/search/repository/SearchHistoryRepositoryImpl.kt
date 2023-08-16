package com.example.playlistmaker.data.search.repository

import android.content.SharedPreferences
import com.example.playlistmaker.domain.search.api.SearchHistoryRepository
import com.example.playlistmaker.domain.search.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistoryRepositoryImpl(
    private val sharedPrefs: SharedPreferences,
    private val gson: Gson
    ) : SearchHistoryRepository {

    private var historyTrackList: ArrayList<Track>

    init {
        historyTrackList =
            createTrackListFromJson(sharedPrefs.getString(SEARCH_HISTORY_KEY, "") ?: "")
    }

    override fun getHistoryTrackList(): List<Track> {
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

    private fun createJsonFromTrackList(trackList: ArrayList<Track>): String {
        return gson.toJson(trackList)
    }

    private fun createTrackListFromJson(json: String): ArrayList<Track> {
        val type = object : TypeToken<ArrayList<Track>>() {}.type
        return gson.fromJson(json, type) ?: arrayListOf()
    }

    companion object {
        private const val LIST_MAX_SIZE = 10

        const val SEARCH_HISTORY_KEY = "SEARCH_HISTORY_KEY"
    }
}