package com.example.playlistmaker.ui.player.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.playlistmaker.databinding.PlaylistVerticalItemBinding
import com.example.playlistmaker.domain.library.playlists.models.Playlist
import com.example.playlistmaker.ui.library.playlists.diff_util.PlaylistDiffUtilCallback
import com.example.playlistmaker.ui.player.view_holder.PlaylistVerticalViewHolder

class PlaylistVerticalAdapter : ListAdapter<Playlist, PlaylistVerticalViewHolder>(
    PlaylistDiffUtilCallback()
) {

    var onAlbumClickListener: ((Playlist) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistVerticalViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        val view = PlaylistVerticalItemBinding.inflate(layoutInspector, parent, false)
        return PlaylistVerticalViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistVerticalViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            onAlbumClickListener?.invoke(getItem(position))
        }
    }
}