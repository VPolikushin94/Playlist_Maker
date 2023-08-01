package com.example.playlistmaker.domain.api.search

import com.example.playlistmaker.domain.models.ResponseResult
import com.example.playlistmaker.domain.models.Track

interface TrackSearchRepository {
    fun searchTrack(expression: String): ResponseResult<List<Track>>
}