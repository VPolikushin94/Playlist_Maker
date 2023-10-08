package com.example.playlistmaker.data.library.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.data.library.db.dao.TrackDao
import com.example.playlistmaker.data.library.db.entity.TrackEntity

@Database(version = 1, entities = [TrackEntity::class])
abstract class AppDatabase : RoomDatabase() {

    abstract fun getTrackDao(): TrackDao
}