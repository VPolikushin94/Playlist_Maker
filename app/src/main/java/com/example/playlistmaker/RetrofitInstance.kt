package com.example.playlistmaker

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val baseUrl = "https://itunes.apple.com/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: MusicApi = retrofit.create(MusicApi::class.java)
}