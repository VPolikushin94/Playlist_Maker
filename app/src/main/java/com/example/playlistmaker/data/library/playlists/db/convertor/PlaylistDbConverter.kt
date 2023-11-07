package com.example.playlistmaker.data.library.playlists.db.convertor

import com.example.playlistmaker.data.library.playlists.db.entity.PlaylistEntity
import com.example.playlistmaker.data.library.playlists.db.entity.PlaylistWithTracks
import com.example.playlistmaker.domain.library.playlists.models.Playlist

class PlaylistDbConverter {
    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlist.playlistId,
            playlist.name,
            playlist.description,
            playlist.imgName
        )
    }

    fun map(playlistEntity: PlaylistEntity): Playlist {
        return Playlist(
            playlistEntity.playlistId,
            playlistEntity.name,
            playlistEntity.description,
            playlistEntity.imgName
        )
    }

    fun map(playlistWithTracks: PlaylistWithTracks): Playlist {
        return Playlist(
            playlistWithTracks.playlist.playlistId,
            playlistWithTracks.playlist.name,
            playlistWithTracks.playlist.description,
            playlistWithTracks.playlist.imgName,
            playlistWithTracks.tracks.size
        )
    }
}