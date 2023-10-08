package com.example.playlistmaker.di

import com.example.playlistmaker.data.library.db.TrackDbConvertor
import com.example.playlistmaker.data.library.repository.FavoritesRepositoryImpl
import com.example.playlistmaker.data.player.repository.AudioPlayerRepositoryImpl
import com.example.playlistmaker.data.search.repository.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.search.repository.TrackSearchRepositoryImpl
import com.example.playlistmaker.data.settings.repository.SettingsRepositoryImpl
import com.example.playlistmaker.data.sharing.repository.SharingRepositoryImpl
import com.example.playlistmaker.domain.library.api.FavoritesRepository
import com.example.playlistmaker.domain.player.api.AudioPlayerRepository
import com.example.playlistmaker.domain.search.api.SearchHistoryRepository
import com.example.playlistmaker.domain.search.api.TrackSearchRepository
import com.example.playlistmaker.domain.settings.api.SettingsRepository
import com.example.playlistmaker.domain.sharing.api.SharingRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {

    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(
            sharedPrefs = get(),
            gson = get(),
            appDatabase = get()
        )
    }

    single<AudioPlayerRepository> {
        AudioPlayerRepositoryImpl(
            mediaPlayer = get()
        )
    }

    single<TrackSearchRepository> {
        TrackSearchRepositoryImpl(
            networkClient = get(),
            appDatabase = get()
        )
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(androidContext())
    }

    single<SharingRepository> {
        SharingRepositoryImpl(
            androidContext(),
            externalNavigator = get()
        )
    }

    factory { TrackDbConvertor() }

    single<FavoritesRepository> {
        FavoritesRepositoryImpl(
            appDatabase = get(),
            trackDbConvertor = get()
        )
    }
}
