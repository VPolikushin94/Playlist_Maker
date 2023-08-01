package com.example.playlistmaker.data.repository

import android.util.Log
import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.domain.models.ResponseResult
import com.example.playlistmaker.data.dto.TrackSearchRequest
import com.example.playlistmaker.data.dto.TrackSearchResponse
import com.example.playlistmaker.data.dto.toTrack
import com.example.playlistmaker.domain.api.search.TrackSearchRepository
import com.example.playlistmaker.domain.models.Track

class TrackSearchRepositoryImpl(private val networkClient: NetworkClient) : TrackSearchRepository {

    override fun searchTrack(expression: String): ResponseResult<List<Track>> {

        val resp = networkClient.doRequest(TrackSearchRequest(expression))

        return if (resp.resultCode == RESULT_OK) {
            val trackList = (resp as TrackSearchResponse).trackList.map { it.toTrack() }

            if (trackList.isEmpty()) {
                ResponseResult.NothingFound()
            } else {
                ResponseResult.Success(trackList)
            }
        } else {
            ResponseResult.Error()
        }
    }

    companion object {
        private const val RESULT_OK = 200
    }
}