package com.example.playlistmaker.ui.library.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentLibraryBinding
import com.example.playlistmaker.ui.library.LibraryViewPagerAdapter
import com.example.playlistmaker.ui.library.models.LibraryScreens
import com.google.android.material.tabs.TabLayoutMediator


class LibraryFragment : Fragment() {

    private var _binding: FragmentLibraryBinding? = null
    private val binding get() = _binding!!

    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vpLibrary.adapter =
            LibraryViewPagerAdapter(this)

        setTabMediator()
    }

    private fun setTabMediator() {
        tabMediator =
            TabLayoutMediator(binding.tlLibrary, binding.vpLibrary) { tab, position ->
                when (position) {
                    LibraryScreens.FAVORITE_SCREEN.ordinal -> tab.text =
                        getString(R.string.favorite_tracks)

                    LibraryScreens.PLAYLIST_SCREEN.ordinal -> tab.text =
                        getString(R.string.playlists)
                }
            }

        tabMediator.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabMediator.detach()
    }
}