package com.example.playlistmaker.ui.search

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.TrackItemBinding
import com.example.playlistmaker.domain.search.models.Track
import java.text.SimpleDateFormat
import java.util.*

class TrackListViewHolder(private val binding: TrackItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(model: Track) {
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.ic_song_placeholder)
            .centerCrop()
            .transform(RoundedCorners(2))
            .into(binding.ivTrackItemImage)
        binding.tvTrackItemName.text = model.trackName
        binding.tvTrackItemArtistName.text = model.artistName
        binding.tvTrackItemTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(model.trackTimeMillis)
    }
}