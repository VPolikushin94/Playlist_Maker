package com.example.playlistmaker.di

import com.example.playlistmaker.ui.library.favorites.view_model.FavoritesViewModel
import com.example.playlistmaker.ui.library.playlists.view_model.PlaylistsViewModel
import com.example.playlistmaker.ui.player.view_model.AudioPlayerViewModel
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        AudioPlayerViewModel(
            audioPlayerInteractor = get(),
            favoritesInteractor = get()
        )
    }

    viewModel {
        SearchViewModel(
            searchInteractor = get()
        )
    }

    viewModel {
        SettingsViewModel(
            settingsInteractor = get(),
            sharingInteractor = get()
        )
    }

    viewModel {
        FavoritesViewModel(
            favoritesInteractor = get()
        )
    }

    viewModel {
        PlaylistsViewModel()
    }
}
