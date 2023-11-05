package com.example.playlistmaker.data.search.repository

import com.example.playlistmaker.data.AppDatabase
import com.example.playlistmaker.data.search.NetworkClient
import com.example.playlistmaker.data.search.dto.TrackSearchRequest
import com.example.playlistmaker.data.search.dto.TrackSearchResponse
import com.example.playlistmaker.data.search.dto.toTrack
import com.example.playlistmaker.domain.search.api.TrackSearchRepository
import com.example.playlistmaker.domain.search.models.Resource
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.util.NetworkResultCode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrackSearchRepositoryImpl(
    private val networkClient: NetworkClient,
    private val appDatabase: AppDatabase
) : TrackSearchRepository {

    private var trackList: List<Track> = emptyList()
    override fun searchTrack(expression: String): Flow<Resource<List<Track>>> = flow {

        val response = networkClient.doRequest(TrackSearchRequest(expression))

        when (response.resultCode) {
            NetworkResultCode.RESULT_OK -> {
                val tracksIdList = appDatabase.getFavoriteTracksDao().getTracksIdList()
                trackList = (response as TrackSearchResponse).trackList.map {
                    if(tracksIdList.contains(it.toTrack().trackId)) {
                        it.toTrack().copy(isFavorite = true)
                    } else {
                        it.toTrack()
                    }
                }

                emit(Resource.Success(trackList))
            }
            NetworkResultCode.RESULT_NO_INTERNET -> emit(Resource.Error("No internet"))
            else -> emit(Resource.Error("Server error"))
        }
    }

    override fun updateSearchTrackList(): Flow<List<Track>> = flow {
        val tracksIdList = appDatabase.getFavoriteTracksDao().getTracksIdList()
        trackList.forEach {
            it.isFavorite = tracksIdList.contains(it.trackId)
        }
        emit(trackList)
    }
}