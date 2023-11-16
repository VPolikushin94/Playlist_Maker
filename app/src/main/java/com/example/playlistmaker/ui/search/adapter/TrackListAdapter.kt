package com.example.playlistmaker.ui.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.playlistmaker.databinding.TrackItemBinding
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.ui.search.diff_util.TrackDiffUtilCallback
import com.example.playlistmaker.ui.search.view_holder.TrackListViewHolder

class TrackListAdapter(
    private val isPlaylistWatcherTracks: Boolean
) : ListAdapter<Track, TrackListViewHolder>(TrackDiffUtilCallback())  {

    var onTrackClickListener: ((Track) -> Unit)? = null
    var onTrackLongClickListener: ((Track) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackListViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        val view = TrackItemBinding.inflate(layoutInspector, parent,false)
        return TrackListViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackListViewHolder, position: Int) {
        holder.bind(getItem(holder.adapterPosition), isPlaylistWatcherTracks)
        holder.itemView.setOnClickListener{
            onTrackClickListener?.invoke(getItem(holder.adapterPosition))
        }
        holder.itemView.setOnLongClickListener {
            onTrackLongClickListener?.invoke(getItem(holder.adapterPosition))
            true
        }
    }
}