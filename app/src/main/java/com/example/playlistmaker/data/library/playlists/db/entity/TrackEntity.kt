package com.example.playlistmaker.data.library.playlists.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_track_table")
data class TrackEntity(
    @PrimaryKey
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: String,
    val artworkUrl60: String,
    val artworkUrl100: String,
    val artworkUrl512: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String,
    val isFavorite: Boolean,
    val addingTime: Long
)