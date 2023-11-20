package com.example.playlistmaker.data.library.playlists.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.playlistmaker.data.library.playlists.db.entity.PlaylistEntity
import com.example.playlistmaker.data.library.playlists.db.entity.PlaylistWithTracks
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlaylist(playlistEntity: PlaylistEntity)

    @Update
    suspend fun updatePlaylist(playlistEntity: PlaylistEntity)

    @Delete
    suspend fun deletePlaylist(playlistEntity: PlaylistEntity)

    @Query("SELECT * FROM playlist_table")
    fun getPlaylistsWithTracks(): Flow<List<PlaylistWithTracks>>

    @Query("SELECT * FROM playlist_table WHERE playlistId=:playlistId")
    fun getPlaylistWithTracksById(playlistId: Int): Flow<PlaylistWithTracks?>
}