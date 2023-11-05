package com.example.playlistmaker.data.library.playlists.db.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class PlaylistWithTracks(
    @Embedded val playlist: PlaylistEntity,
    @Relation(
        parentColumn = "playlistId",
        entity = TrackEntity::class,
        entityColumn = "trackId",
        associateBy = Junction(
            value = PlaylistTrackCrossRef::class,
            parentColumn = "playlistForeignId",
            entityColumn = "trackForeignId"
        ),
    )
    val tracks: List<TrackEntity>
)
