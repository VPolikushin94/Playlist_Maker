package com.example.playlistmaker.data.library.playlists.db.convertor

import com.example.playlistmaker.data.library.playlists.db.entity.TrackEntity
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.util.DateTimeUtil
import java.util.Calendar

class PlaylistTrackDbConvertor {
    fun map(track: Track): TrackEntity {
        return TrackEntity(
            track.trackId,
            track.trackName,
            track.artistName,
            DateTimeUtil.getTimeInMilliseconds(track.trackTimeMillis),
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

    fun map(trackEntity: TrackEntity): Track {
        return Track(
            trackEntity.trackId,
            trackEntity.trackName,
            trackEntity.artistName,
            DateTimeUtil.getFormatTime(trackEntity.trackTimeMillis.toInt()),
            trackEntity.artworkUrl60,
            trackEntity.artworkUrl100,
            trackEntity.artworkUrl512,
            trackEntity.collectionName,
            trackEntity.releaseDate,
            trackEntity.primaryGenreName,
            trackEntity.country,
            trackEntity.previewUrl,
            trackEntity.isFavorite
        )
    }
}