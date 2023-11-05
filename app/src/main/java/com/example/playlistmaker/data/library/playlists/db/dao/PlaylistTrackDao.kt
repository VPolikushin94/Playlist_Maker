package com.example.playlistmaker.data.library.playlists.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Transaction
import com.example.playlistmaker.data.library.playlists.db.entity.PlaylistEntity
import com.example.playlistmaker.data.library.playlists.db.entity.PlaylistTrackCrossRef
import com.example.playlistmaker.data.library.playlists.db.entity.TrackEntity

@Dao
interface PlaylistTrackDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(trackEntity: TrackEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlaylistTrackCrossRef(playlistTrackCrossRef: PlaylistTrackCrossRef): Long

    @Transaction
    suspend fun insertTrackToPlaylist(
        playlistEntity: PlaylistEntity,
        trackEntity: TrackEntity,
    ): Long {
        insertTrack(trackEntity)
        return insertPlaylistTrackCrossRef(
            PlaylistTrackCrossRef(
                playlistEntity.playlistId ?: throw RuntimeException("Empty playlistId in insertPlaylistTrackCrossRef transaction"),
                trackEntity.trackId
            )
        )
    }

    @Delete
    suspend fun deleteTrack(trackEntity: TrackEntity)
}