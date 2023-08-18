package com.example.playlistmaker.ui.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {

    private val settingsViewModel: SettingsViewModel by viewModel()

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setButtonListeners()

        binding.switcherTheme.isChecked = settingsViewModel.isDarkThemeOn()

        binding.switcherTheme.setOnCheckedChangeListener { _, isChecked ->
            settingsViewModel.switchTheme(isChecked)
        }
    }

    private fun setButtonListeners() {
        binding.buttonSettingsBack.setOnClickListener {
            finish()
        }

        binding.buttonShare.setOnClickListener {
            settingsViewModel.shareApp()
        }

        binding.buttonSupport.setOnClickListener {
            settingsViewModel.openSupport()
        }

        binding.buttonLicence.setOnClickListener {
            settingsViewModel.openLicence()
        }
    }
}