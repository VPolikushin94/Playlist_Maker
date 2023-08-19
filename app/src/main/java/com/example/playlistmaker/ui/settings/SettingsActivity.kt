package com.example.playlistmaker.ui.settings

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.ui.settings.models.ShareState
import com.example.playlistmaker.ui.settings.models.ShareType
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

        settingsViewModel.shareState.observe(this) {
            if(it is ShareState.Error) {
                when(it.shareType) {
                    ShareType.SHARE_APP -> showToast(R.string.share_app_error)
                    ShareType.OPEN_SUPPORT -> showToast(R.string.open_support_error)
                    ShareType.OPEN_LICENSE -> showToast(R.string.open_license_error)
                }
            }
        }
    }

    private fun showToast(stringId: Int) {
        Toast.makeText(this, this.getText(stringId),Toast.LENGTH_SHORT).show()
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