package com.example.playlistmaker

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson

class SearchHistory(private val sharedPrefs: SharedPreferences) {

    private val historyTrackList: ArrayList<Track>

    init {
        val historyTrackArray = createTrackListFromJson(
            sharedPrefs.getString(
                SEARCH_HISTORY_KEY,
                ""
            ) ?: ""
        )
        historyTrackList = if (historyTrackArray.isNotEmpty()) {
            historyTrackArray.toList() as ArrayList<Track>
        } else {
            arrayListOf()
        }
    }

    fun getHistoryTrackList(): ArrayList<Track> {
        return historyTrackList
    }

    fun addTrackToHistory(track: Track) {
        if (historyTrackList.size <= LIST_MAX_SIZE) {
            if (checkTrackAvailability(track.trackId)) {
                historyTrackList.remove(track)
            }
            Log.d("TRACK", track.toString())
            historyTrackList.add(track)
            sharedPrefs.edit()
                .putString(SEARCH_HISTORY_KEY, createJsonFromTrackList(historyTrackList))
                .apply()
        }
    }

    fun clearHistoryTrackList() {
        historyTrackList.clear()
        sharedPrefs.edit()
            .remove(SEARCH_HISTORY_KEY)
            .apply()
    }

    private fun checkTrackAvailability(trackId: Int): Boolean {
        return historyTrackList.find {
            it.trackId == trackId
        } != null
    }

    private fun createJsonFromTrackList(trackList: ArrayList<Track>): String {
        return Gson().toJson(trackList)
    }

    private fun createTrackListFromJson(json: String): Array<Track> {
        return Gson().fromJson(json, Array<Track>::class.java) ?: arrayOf()
    }

    companion object {
        private const val LIST_MAX_SIZE = 10

        const val SEARCH_HISTORY_KEY = "SEARCH_HISTORY_KEY"
    }
}