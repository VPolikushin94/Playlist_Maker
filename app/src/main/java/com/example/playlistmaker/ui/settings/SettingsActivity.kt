package com.example.playlistmaker.ui.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var settingsViewModel: SettingsViewModel

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setButtonListeners()

        settingsViewModel = ViewModelProvider(this, SettingsViewModel.getSettingsViewModel())[SettingsViewModel::class.java]

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