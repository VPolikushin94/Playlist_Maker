package com.example.playlistmaker.data.library.favorites.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.library.favorites.db.entity.FavoriteTracksEntity

@Dao
interface FavoriteTracksDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(favoriteTracksEntity: FavoriteTracksEntity)

    @Delete
    suspend fun deleteTrack(favoriteTracksEntity: FavoriteTracksEntity)

    @Query("SELECT * FROM favorite_tracks_table ORDER BY addingTime DESC")
    suspend fun getTrackList(): List<FavoriteTracksEntity>

    @Query("SELECT trackId FROM favorite_tracks_table")
    suspend fun getTracksIdList(): List<Int>
}