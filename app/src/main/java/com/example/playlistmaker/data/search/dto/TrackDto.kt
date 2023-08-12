package com.example.playlistmaker.data.search.dto

import com.example.playlistmaker.domain.search.models.Track

data class TrackDto(
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String
)

fun TrackDto.toTrack(): Track {
    return Track(
        this.trackId,
        this.trackName,
        this.artistName,
        this.trackTimeMillis,
        this.artworkUrl100,
        this.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"),
        this.collectionName,
        this.releaseDate,
        this.primaryGenreName,
        this.country,
        this.previewUrl
    )
}