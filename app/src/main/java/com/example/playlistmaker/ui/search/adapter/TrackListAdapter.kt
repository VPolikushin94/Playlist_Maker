package com.example.playlistmaker.ui.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.TrackItemBinding
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.ui.search.TrackListViewHolder

class TrackListAdapter: RecyclerView.Adapter<TrackListViewHolder>() {

    var trackList = ArrayList<Track>()

    var onTrackClickListener: ((Track) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackListViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        val view = TrackItemBinding.inflate(layoutInspector, parent,false)
        return TrackListViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackListViewHolder, position: Int) {
        holder.bind(trackList[position])
        holder.itemView.setOnClickListener{
            onTrackClickListener?.invoke(trackList[position])
        }
    }

    override fun getItemCount(): Int {
        return trackList.size
    }

}