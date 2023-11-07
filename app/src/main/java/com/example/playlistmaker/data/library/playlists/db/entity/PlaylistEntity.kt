package com.example.playlistmaker.data.library.playlists.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val playlistId: Int?,
    val name: String,
    val description: String?,
    val imgName: String?
)