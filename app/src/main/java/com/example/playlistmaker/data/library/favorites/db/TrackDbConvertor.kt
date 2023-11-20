package com.example.playlistmaker.data.library.favorites.db

import com.example.playlistmaker.data.library.favorites.db.entity.FavoriteTracksEntity
import com.example.playlistmaker.domain.search.models.Track
import java.util.Calendar

class TrackDbConvertor {
    fun map(track: Track): FavoriteTracksEntity {
        return FavoriteTracksEntity(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl60,
            track.artworkUrl100,
            track.artworkUrl512,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
            track.isFavorite,
            Calendar.getInstance().timeInMillis
        )
    }

    fun map(favoriteTracksEntity: FavoriteTracksEntity): Track {
        return Track(
            favoriteTracksEntity.trackId,
            favoriteTracksEntity.trackName,
            favoriteTracksEntity.artistName,
            favoriteTracksEntity.trackTimeMillis,
            favoriteTracksEntity.artworkUrl60,
            favoriteTracksEntity.artworkUrl100,
            favoriteTracksEntity.artworkUrl512,
            favoriteTracksEntity.collectionName,
            favoriteTracksEntity.releaseDate,
            favoriteTracksEntity.primaryGenreName,
            favoriteTracksEntity.country,
            favoriteTracksEntity.previewUrl,
            favoriteTracksEntity.isFavorite
        )
    }
}