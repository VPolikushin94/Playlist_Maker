package com.example.playlistmaker.ui.library.playlists.playlist_creator

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.library.playlists.models.Playlist
import com.example.playlistmaker.ui.library.playlists.playlist_creator.view_model.PlaylistRedactorViewModel
import com.example.playlistmaker.util.ImageSaver
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistRedactorFragment : PlaylistCreatorFragment() {

    override val viewModel: PlaylistRedactorViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val playlist: Playlist = arguments?.getParcelable(PLAYLIST_INFO)
            ?: throw RuntimeException("Empty PLAYLIST_INFO argument")

        setContent(playlist)
        setClickListeners(playlist)
    }

    private fun setClickListeners(playlist: Playlist) {
        binding.buttonPlaylistCreatorBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.buttonCreate.setOnClickListener {
            if (viewModel.clickDebounce()) {
                val image = if(playlist.imgName.isNullOrEmpty() || playlistImageUri != Uri.EMPTY) {
                    getSavedImageName()
                } else {
                    playlist.imgName
                }
                viewModel.addPlaylist(
                    playlist.playlistId,
                    binding.etName.text.toString(),
                    image ?: "",
                    binding.etDescription.text.toString()
                )
            }
        }
    }

    private fun setContent(playlist: Playlist) {
        binding.etName.setText(playlist.name)
        binding.etDescription.setText(playlist.description)
        val img = ImageSaver.getImage(
            requireContext(),
            ALBUM_PICTURES_DIR,
            playlist.imgName ?: ""
        )
        Glide.with(this)
            .load(img)
            .placeholder(R.drawable.ic_add_playlist_image)
            .centerCrop()
            .transform(
                RoundedCorners(
                    requireContext().resources.getDimensionPixelSize(R.dimen.search_grid_album_radius)
                )
            )
            .into(binding.ivAddImage)
        binding.tvScreenName.text = getString(R.string.redact)
        binding.buttonCreate.text = getString(R.string.save)
    }

    companion object {
        private const val PLAYLIST_INFO = "PLAYLIST_INFO"

        fun getPlaylistRedactorBundle(playlist: Playlist): Bundle {
            return bundleOf(PLAYLIST_INFO to playlist)
        }
    }
}