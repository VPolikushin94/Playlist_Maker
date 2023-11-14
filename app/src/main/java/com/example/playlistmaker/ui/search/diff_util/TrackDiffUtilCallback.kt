package com.example.playlistmaker.ui.search.diff_util

import androidx.recyclerview.widget.DiffUtil
import com.example.playlistmaker.domain.search.models.Track

class TrackDiffUtilCallback : DiffUtil.ItemCallback<Track>() {
    override fun areItemsTheSame(oldItem: Track, newItem: Track): Boolean {
        return oldItem.trackId == newItem.trackId
    }

    override fun areContentsTheSame(oldItem: Track, newItem: Track): Boolean {
        return oldItem == newItem
    }
}