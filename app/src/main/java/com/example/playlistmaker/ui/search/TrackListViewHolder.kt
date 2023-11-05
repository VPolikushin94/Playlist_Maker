package com.example.playlistmaker.ui.search

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.TrackItemBinding
import com.example.playlistmaker.domain.search.models.Track

class TrackListViewHolder(private val binding: TrackItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(model: Track) {
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.ic_song_placeholder)
            .centerCrop()
            .transform(
                RoundedCorners(
                    itemView.resources.getDimensionPixelSize(R.dimen.search_vertical_album_radius)
                )
            )
            .into(binding.ivTrackItemImage)
        binding.tvTrackItemName.text = model.trackName
        binding.tvTrackItemArtistName.text = model.artistName
        binding.tvTrackItemTime.text = model.trackTimeMillis
    }
}