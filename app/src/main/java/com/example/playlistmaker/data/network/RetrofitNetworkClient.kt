package com.example.playlistmaker.data.network

import android.net.ConnectivityManager
import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.Response
import com.example.playlistmaker.data.dto.TrackSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitNetworkClient : NetworkClient {

    private val baseUrl = "https://itunes.apple.com/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: MusicApiService = retrofit.create(MusicApiService::class.java)

    override fun doRequest(dto: Any): Response {
        return if (dto is TrackSearchRequest) {

            //TODO добавить проверку наличия сети
            val resp = api.search(dto.expression).execute()
            val body = resp.body() ?: Response()
            body.apply { resultCode = resp.code() }
        } else {
            Response().apply { resultCode = RESULT_ERROR }
        }
    }

//    fun isNetworkAvailable(context: Context): Boolean {
//        val connectivityManager =
//            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        return connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!
//            .isConnected
//    }

    companion object {
        private const val RESULT_ERROR = 400
    }
}