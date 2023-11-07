package com.example.playlistmaker.ui.player

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentAudioPlayerBinding
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.ui.player.adapter.PlaylistVerticalAdapter
import com.example.playlistmaker.ui.player.models.AudioPlayerState
import com.example.playlistmaker.ui.player.models.BottomSheetContentState
import com.example.playlistmaker.ui.player.view_model.AudioPlayerViewModel
import com.example.playlistmaker.util.DateTimeUtil
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel


class AudioPlayerFragment : Fragment() {

    private var _binding: FragmentAudioPlayerBinding? = null
    private val binding get() = _binding!!

    private val playerViewModel: AudioPlayerViewModel by viewModel()

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    private lateinit var playlistAdapter: PlaylistVerticalAdapter

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            exitChecker()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAudioPlayerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playerViewModel.track = arguments?.getParcelable(TRACK_INFO)
            ?: throw RuntimeException("Empty TRACK_INFO argument")

        if (!playerViewModel.isFragmentCreated) {
            playerViewModel.prepare(playerViewModel.track)
            playerViewModel.isFragmentCreated = true
        }

        setRecyclerViewAdapter()
        setContent()
        setBottomSheet()
        setTouchListeners()
        setClickListeners()


        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)

        playerViewModel.getPlaylists()

        playerViewModel.playerState.observe(viewLifecycleOwner) {
            showPlayButtonState(it)
        }

        playerViewModel.favoriteBtnState.observe(viewLifecycleOwner) {
            showFavoriteButtonState(it)
        }

        playerViewModel.bottomSheetContentState.observe(viewLifecycleOwner) {
            renderBottomSheetContent(it)
        }
    }

    override fun onPause() {
        super.onPause()
        playerViewModel.pause()
    }

    private fun exitChecker() {
        if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_HIDDEN) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        } else {
            findNavController().navigateUp()
        }
    }

    private fun setRecyclerViewAdapter() {
        playlistAdapter = PlaylistVerticalAdapter()
        binding.rvPlaylists.adapter = playlistAdapter
    }

    private fun showFavoriteButtonState(isFavorite: Boolean) {
        if (isFavorite) {
            binding.buttonAddToFavorite.setImageResource(R.drawable.ic_add_to_favorite_red)
        } else {
            binding.buttonAddToFavorite.setImageResource(R.drawable.ic_add_to_favorite)
        }
    }

    private fun setContent() {
        binding.tvPlayerTrackName.text = playerViewModel.track.trackName
        binding.tvPlayerArtistName.text = playerViewModel.track.artistName
        binding.tvPlayerTrackTime.text = playerViewModel.track.trackTimeMillis
        if (playerViewModel.track.collectionName.isEmpty()) {
            binding.groupPlayerTrackAlbum.isVisible = false
        } else {
            binding.groupPlayerTrackAlbum.isVisible = true
            binding.tvPlayerTrackAlbum.text = playerViewModel.track.collectionName
        }
        binding.tvPlayerTrackYear.text =
            playerViewModel.track.releaseDate.substring(
                FIRST_DIGIT_OF_YEAR,
                LAST_DIGIT_OF_YEAR
            )
        binding.tvPlayerTrackGenre.text = playerViewModel.track.primaryGenreName
        binding.tvPlayerTrackCountry.text = playerViewModel.track.country

        Glide.with(this)
            .load(playerViewModel.track.artworkUrl512)
            .placeholder(R.drawable.ic_player_track_placeholder)
            .centerCrop()
            .transform(
                RoundedCorners(
                    resources.getDimensionPixelSize(R.dimen.player_album_radius)
                )
            )
            .into(binding.ivPlayerTrackImage)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchListeners() {
        binding.overlay.setOnTouchListener { _, _ ->
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            false
        }
    }

    private fun showPlayButtonState(playerState: AudioPlayerState) {
        when (playerState) {
            is AudioPlayerState.Default -> binding.buttonPlayerPlay.isEnabled = false
            is AudioPlayerState.Prepared -> {
                binding.buttonPlayerPlay.isEnabled = true
                binding.buttonPlayerPlay.setImageResource(R.drawable.ic_play_button)
                showCurrentTrackTime(DateTimeUtil.getFormatTime(TIME_START))
            }

            is AudioPlayerState.Paused -> {
                binding.buttonPlayerPlay.setImageResource(R.drawable.ic_play_button)
                showCurrentTrackTime(playerState.time)
            }

            is AudioPlayerState.Playing -> {
                binding.buttonPlayerPlay.setImageResource(R.drawable.ic_pause_button)
                showCurrentTrackTime(playerState.time)
            }
        }
    }

    private fun renderBottomSheetContent(state: BottomSheetContentState) {
        when(state) {
            is BottomSheetContentState.Loading -> showProgressBar(true)
            is BottomSheetContentState.Content -> {
                showProgressBar(false)

                binding.llPlaylistsPlaceholder.isVisible = state.playlists.isEmpty()
                binding.rvPlaylists.isVisible = state.playlists.isNotEmpty()

                playlistAdapter.submitList(state.playlists)
            }
            is BottomSheetContentState.AddTrackState -> {
                showProgressBar(false)

                showSnackBar(state.isAlreadyAdded, state.playlistName)
            }
        }
    }

    private fun showSnackBar(isAlreadyAdded: Boolean, playlistName: String) {
        val text = if (isAlreadyAdded) {
            "Трек уже добавлен в плейлист $playlistName"
        } else {
            "Добавлено в плейлист $playlistName"
        }

        Snackbar.make(
            binding.root,
            text,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun showProgressBar(isVisible: Boolean) {
        if (isVisible) {
            binding.flPlaylistsContent.isVisible = false
            binding.pbPlaylists.isVisible = true
        } else {
            binding.pbPlaylists.isVisible = false
            binding.flPlaylistsContent.isVisible = true
        }
    }

    private fun showCurrentTrackTime(time: String) {
        binding.tvPlayerTrackCurrentTime.text = time
    }

    private fun setBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.playlistsBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.isVisible = false
                        binding.buttonPlayerBack.isEnabled = true
                    }

                    else -> {
                        binding.overlay.isVisible = true
                        binding.buttonPlayerBack.isEnabled = false
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.overlay.alpha = slideOffset + 1
            }
        })
    }

    private fun setClickListeners() {
        binding.buttonPlayerBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.buttonPlayerPlay.setOnClickListener {
            playerViewModel.playbackControl()
        }
        binding.buttonAddToFavorite.setOnClickListener {
            playerViewModel.onFavoriteClicked(playerViewModel.track)
        }
        binding.buttonAddToPlaylist.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        binding.buttonCreatePlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_audioPlayerFragment_to_playlistCreatorFragment)
        }
        playlistAdapter.onAlbumClickListener = { playlist ->
            playerViewModel.addTrackToPlaylist(playlist, playerViewModel.track)
        }
    }

    companion object {
        private const val FIRST_DIGIT_OF_YEAR = 0
        private const val LAST_DIGIT_OF_YEAR = 4

        private const val TRACK_INFO = "TRACK_INFO"

        private const val TIME_START = 0

        fun getAudioPlayerBundle(track: Track): Bundle {
            return bundleOf(TRACK_INFO to track)
        }
    }
}