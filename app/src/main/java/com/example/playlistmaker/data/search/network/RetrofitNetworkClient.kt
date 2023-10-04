package com.example.playlistmaker.data.search.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.playlistmaker.data.search.NetworkClient
import com.example.playlistmaker.data.search.dto.Response
import com.example.playlistmaker.data.search.dto.TrackSearchRequest
import com.example.playlistmaker.util.NetworkResultCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class RetrofitNetworkClient(
    private val context: Context,
    private val apiService: MusicApiService
) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        if (!isConnected()) {
            return Response().apply { resultCode = NetworkResultCode.RESULT_NO_INTERNET }
        }

        if (dto !is TrackSearchRequest) {
            return Response().apply { resultCode = NetworkResultCode.RESULT_ERROR }
        }

        return withContext(Dispatchers.IO) {
            try {
                val resp = apiService.search(dto.expression)
                resp.apply { resultCode = NetworkResultCode.RESULT_OK }
            } catch (e: Throwable) {
                Response().apply { resultCode = NetworkResultCode.RESULT_ERROR }
            }
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }

}