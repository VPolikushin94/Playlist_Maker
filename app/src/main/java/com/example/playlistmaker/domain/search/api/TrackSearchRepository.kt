package com.example.playlistmaker.domain.search.api

import com.example.playlistmaker.domain.search.models.Resource
import com.example.playlistmaker.domain.search.models.Track

interface TrackSearchRepository {
    fun searchTrack(expression: String): Resource<List<Track>>
}