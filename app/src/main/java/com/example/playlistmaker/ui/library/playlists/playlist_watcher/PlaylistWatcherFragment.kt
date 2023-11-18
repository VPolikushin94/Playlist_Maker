package com.example.playlistmaker.ui.library.playlists.playlist_watcher

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.core.view.doOnNextLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistWatcherBinding
import com.example.playlistmaker.domain.library.playlists.models.Playlist
import com.example.playlistmaker.domain.library.playlists.models.PlaylistAndTracks
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.ui.library.playlists.playlist_creator.PlaylistCreatorFragment
import com.example.playlistmaker.ui.library.playlists.playlist_creator.PlaylistRedactorFragment
import com.example.playlistmaker.ui.library.playlists.playlist_watcher.models.PlaylistWatcherScreenState
import com.example.playlistmaker.ui.library.playlists.playlist_watcher.view_model.PlaylistWatcherViewModel
import com.example.playlistmaker.ui.player.AudioPlayerFragment
import com.example.playlistmaker.ui.search.adapter.TrackListAdapter
import com.example.playlistmaker.ui.settings.models.ShareState
import com.example.playlistmaker.ui.settings.models.ShareType
import com.example.playlistmaker.util.ImageSaver
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlaylistWatcherFragment : Fragment() {

    private var _binding: FragmentPlaylistWatcherBinding? = null
    private val binding get() = _binding!!

    private val playlistWatcherViewModel: PlaylistWatcherViewModel by viewModel()

    private lateinit var trackAdapter: TrackListAdapter
    private lateinit var deleteTrackConfirmDialog: MaterialAlertDialogBuilder
    private lateinit var deletePlaylistConfirmDialog: MaterialAlertDialogBuilder
    private lateinit var trackToDelete: Track

    private lateinit var playlistInfoBottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var trackListBottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            exitChecker()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPlaylistWatcherBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.doOnNextLayout { calculatePeekHeight() }

        val playlist: Playlist = arguments?.getParcelable(PLAYLIST_INFO)
            ?: throw RuntimeException("Empty PLAYLIST_INFO argument")


        setDeleteTrackConfirmDialog()
        setDeletePlaylistConfirmDialog()
        setRecyclerViewAdapter()
        setClickListeners()
        setPlaylistBottomSheet()
        setTracksBottomSheet()
        setTouchListeners()

        playlistWatcherViewModel.updatePlayListAndTracks(
            playlist.playlistId ?: throw RuntimeException("Empty playlistId")
        )

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)

        playlistWatcherViewModel.playlistWatcherScreenState.observe(viewLifecycleOwner) {
            render(it)
        }

        playlistWatcherViewModel.shareState.observe(viewLifecycleOwner) {
            if (it is ShareState.Error) {
                when (it.shareType) {
                    ShareType.SHARE_PLAYLIST -> showSnackBar(getString(R.string.share_playlist_error))
                    else -> throw RuntimeException("Wrong share type")
                }
            }
        }
    }

    private fun setTracksBottomSheet() {
        trackListBottomSheetBehavior = BottomSheetBehavior.from(binding.tracksBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

    private fun setPlaylistBottomSheet() {
        playlistInfoBottomSheetBehavior = BottomSheetBehavior.from(binding.playlistBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        playlistInfoBottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.isVisible = false
                        binding.buttonPlaylistBack.isEnabled = true
                    }

                    else -> {
                        binding.overlay.isVisible = true
                        binding.buttonPlaylistBack.isEnabled = false
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.overlay.alpha = slideOffset + 1
            }
        })
    }

    private fun exitChecker() {
        if (playlistInfoBottomSheetBehavior.state != BottomSheetBehavior.STATE_HIDDEN) {
            playlistInfoBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        } else {
            findNavController().navigateUp()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchListeners() {
        binding.overlay.setOnTouchListener { _, _ ->
            playlistInfoBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            false
        }
    }

    private fun showSnackBar(text: String) {
        Snackbar.make(
            binding.root,
            text,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun setDeleteTrackConfirmDialog() {
        deleteTrackConfirmDialog = MaterialAlertDialogBuilder(requireContext(), R.style.MyAlertDialog)
            .setTitle(getString(R.string.delete_track))
            .setNegativeButton(getString(R.string.not)) { dialog, _ ->
                dialog.cancel()
            }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                playlistWatcherViewModel.deleteTrack(
                    playlistWatcherViewModel.currentPlaylist.playlist,
                    trackToDelete
                )
            }

    }

    private fun setDeletePlaylistConfirmDialog() {
        deletePlaylistConfirmDialog = MaterialAlertDialogBuilder(requireContext(), R.style.MyAlertDialog)
            .setNegativeButton(getString(R.string.not)) { dialog, _ ->
                dialog.cancel()
            }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                playlistWatcherViewModel.deletePlaylist(playlistWatcherViewModel.currentPlaylist)
            }

    }

    private fun calculatePeekHeight() {
        val screenHeight = binding.root.height

        val peekHeight = screenHeight - (binding.buttonPlaylistShare.bottom + resources.getDimensionPixelSize(R.dimen.bottom_sheet_padding_top))
        trackListBottomSheetBehavior.peekHeight = if (peekHeight < MIN_PEEK_HEIGHT) {
            MIN_PEEK_HEIGHT
        } else {
            peekHeight
        }
    }

    private fun setClickListeners() {
        binding.buttonPlaylistBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.buttonPlaylistMenu.setOnClickListener {
            playlistInfoBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        binding.buttonPlaylistShare.setOnClickListener {
            sharePlaylist(playlistWatcherViewModel.currentPlaylist)
        }

        binding.buttonBsShare.setOnClickListener {
            sharePlaylist(playlistWatcherViewModel.currentPlaylist)
        }
        binding.buttonBsRedactInfo.setOnClickListener {
            findNavController().navigate(
                R.id.playlistRedactorFragment,
                PlaylistRedactorFragment.getPlaylistRedactorBundle(playlistWatcherViewModel.currentPlaylist.playlist)
            )
        }
        binding.buttonBsDeletePlaylist.setOnClickListener {
            deletePlaylistConfirmDialog.show()
        }

        trackAdapter.onTrackClickListener = {
            findNavController().navigate(
                R.id.action_playlistWatcherFragment_to_audioPlayerFragment,
                AudioPlayerFragment.getAudioPlayerBundle(it)
            )
        }
        trackAdapter.onTrackLongClickListener = {
            trackToDelete = it
            deleteTrackConfirmDialog.show()
        }
    }

    private fun sharePlaylist(playlistAndTracks: PlaylistAndTracks) {
        if (playlistAndTracks.tracks.isEmpty()) {
            showSnackBar(getString(R.string.empty_track_list))
        } else {
            playlistWatcherViewModel.sharePlaylist(playlistAndTracks)
        }
    }

    private fun setRecyclerViewAdapter() {
        trackAdapter = TrackListAdapter(true)
        binding.rvTracks.adapter = trackAdapter
    }

    private fun setContent(playlist: Playlist) {
        deletePlaylistConfirmDialog.setTitle(
            getString(
                R.string.delete_playlist_dialog_title,
                playlist.name
            )
        )

        val img = ImageSaver.getImage(
            requireContext(),
            PlaylistCreatorFragment.ALBUM_PICTURES_DIR,
            playlist.imgName ?: ""
        )
        Glide.with(this)
            .load(img)
            .placeholder(R.drawable.ic_song_placeholder)
            .centerCrop()
            .into(binding.ivPlaylistImage)

        binding.tvPlaylistName.text = playlist.name
        binding.tvPlaylistDescription.text = playlist.description
        binding.tvPlaylistMinutes.text = resources.getQuantityString(
            R.plurals.playlist_time,
            playlist.playlistTime ?: 0,
            playlist.playlistTime ?: 0
        )
        binding.tvPlaylistTracksCounter.text =
            resources.getQuantityString(
                R.plurals.playlist_track_number,
                playlist.tracksCounter ?: 0,
                playlist.tracksCounter ?: 0
            )

        binding.playlistInfo.tvPlaylistItemName.text = playlist.name
        binding.playlistInfo.tvPlaylistItemTracksCounter.text = resources.getQuantityString(
            R.plurals.playlist_track_number,
            playlist.tracksCounter ?: 0,
            playlist.tracksCounter ?: 0
        )
        Glide.with(this)
            .load(img)
            .placeholder(R.drawable.ic_song_placeholder)
            .centerCrop()
            .transform(
                RoundedCorners(
                    resources.getDimensionPixelSize(R.dimen.search_vertical_album_radius)
                )
            )
            .into(binding.playlistInfo.ivPlaylistItemImage)
    }

    private fun render(state: PlaylistWatcherScreenState) {
        when (state) {
            is PlaylistWatcherScreenState.Loading -> showProgressBar(true)
            is PlaylistWatcherScreenState.Content -> {
                showProgressBar(false)
                val trackList = state.playlistAndTracks.tracks

                updateTracksBottomSheetState(trackList)

                updateTracksBottomSheetContent(trackList)

                setContent(state.playlistAndTracks.playlist)
            }
            is PlaylistWatcherScreenState.Close -> findNavController().navigateUp()
        }
    }

    private fun updateTracksBottomSheetState(trackList: List<Track>) {
        if (trackList.isEmpty()) {
            trackListBottomSheetBehavior.isHideable = true
            trackListBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            showSnackBar(getString(R.string.empty_track_list))
        } else if(trackListBottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED){
            trackListBottomSheetBehavior.isHideable = false
            trackListBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    private fun updateTracksBottomSheetContent(trackList: List<Track>) {
        binding.llTracksPlaceholder.isVisible = trackList.isEmpty()
        binding.rvTracks.isVisible = trackList.isNotEmpty()
        trackAdapter.submitList(trackList)
    }

    private fun showProgressBar(isVisible: Boolean) {
        if (isVisible) {
            binding.flTracksContent.isVisible = false
            binding.pbTracks.isVisible = true
        } else {
            binding.pbTracks.isVisible = false
            binding.flTracksContent.isVisible = true
        }
    }

    companion object {
        private const val PLAYLIST_INFO = "PLAYLIST_INFO"

        private const val MIN_PEEK_HEIGHT = 80

        fun getPlaylistWatcherBundle(playlist: Playlist): Bundle {
            return bundleOf(PLAYLIST_INFO to playlist)
        }
    }
}