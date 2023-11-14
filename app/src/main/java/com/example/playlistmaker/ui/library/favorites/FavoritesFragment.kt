package com.example.playlistmaker.ui.library.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFavoritesBinding
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.ui.library.favorites.models.FavoritesScreenState
import com.example.playlistmaker.ui.library.favorites.view_model.FavoritesViewModel
import com.example.playlistmaker.ui.player.AudioPlayerFragment
import com.example.playlistmaker.ui.search.adapter.TrackListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private var trackListAdapter: TrackListAdapter? = null

    private val favoritesViewModel: FavoritesViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerViewAdapters()
        setTrackClickListener()

        favoritesViewModel.favoritesScreenState.observe(viewLifecycleOwner) {
            render(it)
        }
    }

    override fun onStart() {
        super.onStart()
        favoritesViewModel.getFavoritesList()
    }

    private fun render(state: FavoritesScreenState) {
        when(state) {
            is FavoritesScreenState.Loading -> showProgressBar(true)
            is FavoritesScreenState.Content -> {
                showProgressBar(false)

                binding.llFavoritesPlaceholder.isVisible = state.isEmpty
                binding.rvFavoritesList.isVisible = !state.isEmpty
                trackListAdapter?.submitList(state.trackList)
            }
        }

    }

    private fun showProgressBar(isVisible: Boolean) {
        if (isVisible) {
            binding.flFavoritesContent.isVisible = false
            binding.pbFavorites.isVisible = true
        } else {
            binding.pbFavorites.isVisible = false
            binding.flFavoritesContent.isVisible = true
        }
    }

    private fun setRecyclerViewAdapters() {
        trackListAdapter = TrackListAdapter()
        binding.rvFavoritesList.itemAnimator = null
        binding.rvFavoritesList.adapter = trackListAdapter
    }

    private fun setTrackClickListener() {
        trackListAdapter?.onTrackClickListener = {
            clickTrack(it)
        }
    }

    private fun clickTrack(track: Track) {
        if (favoritesViewModel.clickDebounce()) {
            findNavController().navigate(
                R.id.action_libraryFragment_to_audioPlayerFragment,
                AudioPlayerFragment.getAudioPlayerBundle(track)
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.rvFavoritesList.adapter = null
        trackListAdapter = null
        _binding = null
    }

    companion object {
        fun newInstance(): FavoritesFragment {
            val args = Bundle()

            val fragment = FavoritesFragment()
            fragment.arguments = args
            return fragment
        }
    }
}