package com.example.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import com.example.playlistmaker.data.search.NetworkClient
import com.example.playlistmaker.data.search.network.MusicApiService
import com.example.playlistmaker.data.search.network.RetrofitNetworkClient
import com.example.playlistmaker.data.sharing.ExternalNavigator
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    single<MusicApiService> {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MusicApiService::class.java)
    }

    single {
        androidContext().getSharedPreferences(
            PLAY_LIST_MAKER_SHARED_PREFERENCES,
            Context.MODE_PRIVATE
        )
    }

    factory { Gson() }

    single { MediaPlayer() }

    single {
        ExternalNavigator(androidContext())
    }

    single<NetworkClient> {
        RetrofitNetworkClient(
            androidContext(),
            apiService = get()
        )
    }

}


private const val baseUrl = "https://itunes.apple.com/"

private const val PLAY_LIST_MAKER_SHARED_PREFERENCES = "PLAY_LIST_MAKER_SHARED_PREFERENCES"


