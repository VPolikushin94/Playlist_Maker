package com.example.playlistmaker.ui.library.playlists.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.playlistmaker.databinding.PlaylistGridItemBinding
import com.example.playlistmaker.domain.library.playlists.models.Playlist
import com.example.playlistmaker.ui.library.playlists.diff_util.PlaylistDiffUtilCallback
import com.example.playlistmaker.ui.library.playlists.view_holder.PlaylistGridViewHolder

class PlaylistGridAdapter : ListAdapter<Playlist, PlaylistGridViewHolder>(PlaylistDiffUtilCallback()) {

    var onAlbumClickListener: ((Playlist) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistGridViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        val view = PlaylistGridItemBinding.inflate(layoutInspector, parent, false)
        return PlaylistGridViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistGridViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            onAlbumClickListener?.invoke(getItem(position))
        }
    }
}