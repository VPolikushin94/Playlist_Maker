package com.example.playlistmaker.ui.library.playlists.view_holder

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistGridItemBinding
import com.example.playlistmaker.domain.library.playlists.models.Playlist
import com.example.playlistmaker.ui.library.playlists.playlist_creator.PlaylistCreatorFragment
import com.example.playlistmaker.util.ImageSaver

class PlaylistGridViewHolder(private val binding: PlaylistGridItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(model: Playlist) {
        val img = ImageSaver.getImage(
            itemView.context,
            PlaylistCreatorFragment.ALBUM_PICTURES_DIR,
            model.imgName ?: ""
        )

        Glide.with(itemView)
            .load(img)
            .placeholder(R.drawable.ic_song_placeholder)
            .centerCrop()
            .transform(
                RoundedCorners(
                    itemView.resources.getDimensionPixelSize(R.dimen.search_grid_album_radius)
                )
            )
            .into(binding.ivPlaylistItemImage)

        binding.tvPlaylistItemName.text = model.name
        binding.tvPlaylistItemTracksCounter.text = itemView.resources.getQuantityString(
            R.plurals.playlist_track_number,
            model.tracksCounter ?: 0,
            model.tracksCounter ?: 0
        )
    }
}