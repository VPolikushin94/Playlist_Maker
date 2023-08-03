package com.example.playlistmaker.presentation.ui.search

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.*

class TrackListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val ivTrackImage = itemView.findViewById<ImageView>(R.id.iv_track_item_image)
    private val tvTrackName = itemView.findViewById<TextView>(R.id.tv_track_item_name)
    private val tvArtistName = itemView.findViewById<TextView>(R.id.tv_track_item_artist_name)
    private val tvTrackTime = itemView.findViewById<TextView>(R.id.tv_track_item_time)

    fun bind(model: Track) {
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.ic_song_placeholder)
            .centerCrop()
            .transform(RoundedCorners(2))
            .into(ivTrackImage)
        tvTrackName.text = model.trackName
        tvArtistName.text = model.artistName
        tvTrackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(model.trackTimeMillis)
    }
}