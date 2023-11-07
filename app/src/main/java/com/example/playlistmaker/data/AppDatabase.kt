package com.example.playlistmaker.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.data.library.favorites.db.dao.FavoriteTracksDao
import com.example.playlistmaker.data.library.favorites.db.entity.FavoriteTracksEntity
import com.example.playlistmaker.data.library.playlists.db.dao.PlaylistDao
import com.example.playlistmaker.data.library.playlists.db.dao.PlaylistTrackDao
import com.example.playlistmaker.data.library.playlists.db.entity.PlaylistEntity
import com.example.playlistmaker.data.library.playlists.db.entity.PlaylistTrackCrossRef
import com.example.playlistmaker.data.library.playlists.db.entity.TrackEntity


@Database(
    version = 1,
    entities = [
        FavoriteTracksEntity::class,
        TrackEntity::class,
        PlaylistEntity::class,
        PlaylistTrackCrossRef::class
    ]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getFavoriteTracksDao(): FavoriteTracksDao

    abstract fun getPlaylistTrackDao(): PlaylistTrackDao

    abstract fun getPlaylistDao(): PlaylistDao
}