package com.example.playlistmaker.ui.library.playlists.diff_util

import androidx.recyclerview.widget.DiffUtil
import com.example.playlistmaker.domain.library.playlists.models.Playlist

class PlaylistDiffUtilCallback : DiffUtil.ItemCallback<Playlist>() {
    override fun areItemsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
        return oldItem.playlistId == newItem.playlistId
    }

    override fun areContentsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
        return oldItem == newItem
    }
}