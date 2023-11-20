package com.example.playlistmaker.data.library.playlists.db.convertor

import com.example.playlistmaker.data.library.playlists.db.entity.PlaylistEntity
import com.example.playlistmaker.data.library.playlists.db.entity.PlaylistWithTracks
import com.example.playlistmaker.data.library.playlists.db.entity.TrackEntity
import com.example.playlistmaker.domain.library.playlists.models.Playlist
import com.example.playlistmaker.util.DateTimeUtil

class PlaylistDbConverter {
    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlist.playlistId,
            playlist.name,
            playlist.description,
            playlist.imgName
        )
    }

    private fun getPlaylistTime(trackList: List<TrackEntity>): Int {
        val sum = trackList.sumOf { it.trackTimeMillis.toInt() }
        return DateTimeUtil.getTimeInMinutes(sum)
    }

    fun map(playlistWithTracks: PlaylistWithTracks): Playlist {
        return Playlist(
            playlistWithTracks.playlist.playlistId,
            playlistWithTracks.playlist.name,
            playlistWithTracks.playlist.description,
            playlistWithTracks.playlist.imgName,
            playlistWithTracks.tracks.size,
            getPlaylistTime(playlistWithTracks.tracks)
        )
    }
}