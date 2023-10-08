package com.example.playlistmaker.data.library.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.library.db.entity.TrackEntity

@Dao
interface TrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(trackEntity: TrackEntity)

    @Delete
    suspend fun deleteTrack(trackEntity: TrackEntity)

    @Query("SELECT * FROM track_table ORDER BY addingTime DESC")
    suspend fun getTrackList(): List<TrackEntity>

    @Query("SELECT trackId FROM track_table")
    suspend fun getTracksIdList(): List<Int>
}