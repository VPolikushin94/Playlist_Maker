package com.example.playlistmaker.ui.main

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityRootBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.Locale

class RootActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRootBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_fragment_container) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNavView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.playlistCreatorFragment, R.id.audioPlayerFragment, R.id.playlistWatcherFragment, R.id.playlistRedactorFragment -> {
                    bottomNavView.isVisible = false
                    binding.separator.isVisible = false
                }

                else -> {
                    bottomNavView.isVisible = true
                    binding.separator.isVisible = true
                }
            }

        }
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(setRussianLocal(newBase))
    }

    private fun setRussianLocal(context: Context?): Context? {
        return if (context != null) {
            val local = Locale("ru")
            val config = context.resources.configuration
            config.setLocale(local)
            config.setLayoutDirection(local)
            context.createConfigurationContext(config)
        } else {
            null
        }
    }
}