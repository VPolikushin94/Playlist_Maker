package com.example.playlistmaker.ui.library.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.playlistmaker.ui.library.favorites.FavoritesFragment
import com.example.playlistmaker.ui.library.playlists.PlaylistsFragment
import com.example.playlistmaker.ui.library.models.LibraryScreens

class LibraryViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return ITEM_COUNT
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            LibraryScreens.FAVORITE_SCREEN.ordinal -> FavoritesFragment.newInstance()
            else -> PlaylistsFragment.newInstance()
        }
    }

    companion object {
        private const val ITEM_COUNT = 2
    }
}