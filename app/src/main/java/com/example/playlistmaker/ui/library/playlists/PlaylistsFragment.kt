package com.example.playlistmaker.ui.library.playlists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.ui.library.playlists.adapter.PlaylistGridAdapter
import com.example.playlistmaker.ui.library.playlists.models.PlaylistsScreenState
import com.example.playlistmaker.ui.library.playlists.view_model.PlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {

    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!

    private val playlistsViewModel: PlaylistsViewModel by viewModel()

    private lateinit var playlistAdapter: PlaylistGridAdapter

    override fun onStart() {
        super.onStart()
        playlistsViewModel.getPlaylists()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerViewAdapter()
        setClickListeners()

        playlistsViewModel.playlistsScreenState.observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun setRecyclerViewAdapter() {
        playlistAdapter = PlaylistGridAdapter()
        binding.rvPlaylists.adapter = playlistAdapter
    }

    private fun setClickListeners() {
        binding.buttonCreatePlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_libraryFragment_to_playlistCreatorFragment)
        }

        playlistAdapter.onAlbumClickListener = {

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun render(state: PlaylistsScreenState) {
        when (state) {
            is PlaylistsScreenState.Loading -> showProgressBar(true)
            is PlaylistsScreenState.Content -> {
                showProgressBar(false)

                binding.llPlaylistsPlaceholder.isVisible = state.playlists.isEmpty()
                binding.rvPlaylists.isVisible = state.playlists.isNotEmpty()

                playlistAdapter.submitList(state.playlists)
            }
        }
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

    companion object {
        fun newInstance(): PlaylistsFragment {
            val args = Bundle()

            val fragment = PlaylistsFragment()
            fragment.arguments = args
            return fragment
        }
    }
}