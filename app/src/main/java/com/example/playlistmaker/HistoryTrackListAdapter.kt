package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class HistoryTrackListAdapter : RecyclerView.Adapter<TrackListViewHolder>() {

    var historyTrackList = ArrayList<Track>()

    var onTrackClickListener: ((Track) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return TrackListViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackListViewHolder, position: Int) {
        holder.bind(historyTrackList[position])
        holder.itemView.setOnClickListener {
            onTrackClickListener?.invoke(historyTrackList[position])
        }
    }

    override fun getItemCount(): Int {
        return historyTrackList.size
    }

}