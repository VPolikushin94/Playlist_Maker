package com.example.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val ivTrackImage = itemView.findViewById<ImageView>(R.id.iv_track_image)
    private val tvTrackName = itemView.findViewById<TextView>(R.id.tv_track_name)
    private val tvArtistName = itemView.findViewById<TextView>(R.id.tv_artist_name)
    private val tvTrackTime = itemView.findViewById<TextView>(R.id.tv_track_time)

    fun bind(model: Track) {
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.ic_placeholder)
            .centerCrop()
            .transform(RoundedCorners(2))
            .into(ivTrackImage)
        tvTrackName.text = model.trackName
        tvArtistName.text = model.artistName
        tvTrackTime.text = model.trackTime
    }
}