package com.example.playlistmaker.domain.library.playlists.models

import android.os.Parcelable
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlaylistAndTracks(
    val playlist: Playlist,
    val tracks: List<Track>
) : Parcelable
