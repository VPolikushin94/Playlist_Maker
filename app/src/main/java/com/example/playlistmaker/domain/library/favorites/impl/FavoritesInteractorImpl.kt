package com.example.playlistmaker.domain.library.favorites.impl

import com.example.playlistmaker.domain.library.favorites.api.FavoritesInteractor
import com.example.playlistmaker.domain.library.favorites.api.FavoritesRepository
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

class FavoritesInteractorImpl(private val favoritesRepository: FavoritesRepository) :
    FavoritesInteractor {

    override suspend fun addTrack(track: Track) {
        favoritesRepository.addTrack(track)
    }

    override suspend fun deleteTrack(track: Track) {
        favoritesRepository.deleteTrack(track)
    }

    override fun getTrackList(): Flow<List<Track>> {
        return favoritesRepository.getTrackList()
    }
}