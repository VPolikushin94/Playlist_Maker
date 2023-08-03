package com.example.playlistmaker.presentation.ui.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.presentation.ui.search.TrackListViewHolder
import com.example.playlistmaker.domain.models.Track

class TrackListAdapter: RecyclerView.Adapter<TrackListViewHolder>() {

    var trackList = ArrayList<Track>()

    var onTrackClickListener: ((Track) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
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