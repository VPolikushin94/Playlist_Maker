package com.example.playlistmaker.di

import com.example.playlistmaker.domain.library.favorites.api.FavoritesInteractor
import com.example.playlistmaker.domain.library.favorites.impl.FavoritesInteractorImpl
import com.example.playlistmaker.domain.player.api.AudioPlayerInteractor
import com.example.playlistmaker.domain.player.impl.AudioPlayerInteractorImpl
import com.example.playlistmaker.domain.search.api.SearchInteractor
import com.example.playlistmaker.domain.search.impl.SearchInteractorImpl
import com.example.playlistmaker.domain.settings.api.SettingsInteractor
import com.example.playlistmaker.domain.settings.impl.SettingsInteractorImpl
import com.example.playlistmaker.domain.sharing.api.SharingInteractor
import com.example.playlistmaker.domain.sharing.impl.SharingInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    single<AudioPlayerInteractor> {
        AudioPlayerInteractorImpl(
            audioPlayerRepository = get()
        )
    }

    single<SearchInteractor> {
        SearchInteractorImpl(
            searchHistoryRepository = get(),
            trackSearchRepository = get()
        )
    }

    single<SettingsInteractor> {
        SettingsInteractorImpl(
            settingsRepository = get()
        )
    }

    single<SharingInteractor> {
        SharingInteractorImpl(
            sharingRepository = get()
        )
    }

    single<FavoritesInteractor> {
        FavoritesInteractorImpl(
            favoritesRepository = get()
        )
    }

}
