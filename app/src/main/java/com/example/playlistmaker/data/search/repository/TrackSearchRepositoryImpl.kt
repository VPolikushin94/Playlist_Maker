package com.example.playlistmaker.data.search.repository

import com.example.playlistmaker.data.search.NetworkClient
import com.example.playlistmaker.domain.search.models.Resource
import com.example.playlistmaker.data.search.dto.TrackSearchRequest
import com.example.playlistmaker.data.search.dto.TrackSearchResponse
import com.example.playlistmaker.data.search.dto.toTrack
import com.example.playlistmaker.domain.search.api.TrackSearchRepository
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.util.NetworkResultCode

class TrackSearchRepositoryImpl(private val networkClient: NetworkClient) : TrackSearchRepository {

    override fun searchTrack(expression: String): Resource<List<Track>> {

        val response = networkClient.doRequest(TrackSearchRequest(expression))

        return when(response.resultCode) {
            NetworkResultCode.RESULT_OK -> {
                val trackList = (response as TrackSearchResponse).trackList.map { it.toTrack() }

                Resource.Success(trackList)
            }
            NetworkResultCode.RESULT_NO_INTERNET -> Resource.Error("No internet")
            else -> Resource.Error("Server error")
        }
    }
}