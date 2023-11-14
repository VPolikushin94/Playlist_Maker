package com.example.playlistmaker.domain.library.playlists.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Playlist(
    val playlistId: Int? = null,
    val name: String,
    var description: String? = null,
    var imgName: String? = null,
    var tracksCounter: Int? = null,
    var playlistTime: Int? = null,
) : Parcelable
