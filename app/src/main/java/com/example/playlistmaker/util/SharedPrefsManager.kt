package com.example.playlistmaker.util

import android.content.Context
import android.content.SharedPreferences

object SharedPrefsManager {
    const val THEME_SHARED_PREFERENCES = "THEME_SHARED_PREFERENCES"
    const val SEARCH_SHARED_PREFERENCES = "SEARCH_SHARED_PREFERENCES"

    var searchInstance: SharedPreferences? = null
    var themeInstance: SharedPreferences? = null

    fun init(context: Context, key: String) {
        when(key) {
            THEME_SHARED_PREFERENCES -> themeInstance = context.getSharedPreferences(
                THEME_SHARED_PREFERENCES, Context.MODE_PRIVATE)
            SEARCH_SHARED_PREFERENCES -> searchInstance = context.getSharedPreferences(
                SEARCH_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        }

    }
}