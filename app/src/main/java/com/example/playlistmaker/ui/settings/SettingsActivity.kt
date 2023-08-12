package com.example.playlistmaker.ui.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.R
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {

    private lateinit var buttonSettingsBack: ImageButton
    private lateinit var buttonShare: Button
    private lateinit var buttonSupport: Button
    private lateinit var buttonLicence: Button
    private lateinit var switcherDarkTheme: SwitchMaterial

    private lateinit var settingsViewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        setViews()
        setButtonListeners()

        settingsViewModel = ViewModelProvider(this, SettingsViewModel.getSettingsViewModel())[SettingsViewModel::class.java]

        switcherDarkTheme.isChecked = settingsViewModel.isDarkThemeOn()

        switcherDarkTheme.setOnCheckedChangeListener { _, isChecked ->
            settingsViewModel.switchTheme(isChecked)
        }
    }

    private fun setViews() {
        buttonSettingsBack = findViewById(R.id.button_settings_back)
        buttonShare = findViewById(R.id.button_share)
        buttonSupport = findViewById(R.id.button_support)
        buttonLicence = findViewById(R.id.button_licence)
        switcherDarkTheme = findViewById(R.id.switcher_theme)
    }

    private fun setButtonListeners() {
        buttonSettingsBack.setOnClickListener {
            finish()
        }

        buttonShare.setOnClickListener {
            settingsViewModel.shareApp()
        }

        buttonSupport.setOnClickListener {
            settingsViewModel.openSupport()
        }

        buttonLicence.setOnClickListener {
            settingsViewModel.openLicence()
        }
    }
}