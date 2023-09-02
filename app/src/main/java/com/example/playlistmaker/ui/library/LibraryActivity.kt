package com.example.playlistmaker.ui.library

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityLibraryBinding
import com.example.playlistmaker.ui.library.models.LibraryScreens
import com.google.android.material.tabs.TabLayoutMediator

class LibraryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLibraryBinding
    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.vpLibrary.adapter =
            LibraryViewPagerAdapter(supportFragmentManager, lifecycle)

        setTabMediator()

        setButtonListeners()
    }

    private fun setButtonListeners() {
        binding.buttonLibraryBack.setOnClickListener {
            finish()
        }
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

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}