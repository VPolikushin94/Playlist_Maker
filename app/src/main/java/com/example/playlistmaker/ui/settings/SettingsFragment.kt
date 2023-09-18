package com.example.playlistmaker.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.ui.settings.models.ShareState
import com.example.playlistmaker.ui.settings.models.ShareType
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val settingsViewModel: SettingsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setButtonListeners()

        binding.switcherTheme.isChecked = settingsViewModel.isDarkThemeOn()

        binding.switcherTheme.setOnCheckedChangeListener { _, isChecked ->
            settingsViewModel.switchTheme(isChecked)
        }

        settingsViewModel.shareState.observe(viewLifecycleOwner) {
            if (it is ShareState.Error) {
                when (it.shareType) {
                    ShareType.SHARE_APP -> showToast(R.string.share_app_error)
                    ShareType.OPEN_SUPPORT -> showToast(R.string.open_support_error)
                    ShareType.OPEN_LICENSE -> showToast(R.string.open_license_error)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showToast(stringId: Int) {
        Toast.makeText(requireContext(), this.getText(stringId), Toast.LENGTH_SHORT).show()
    }

    private fun setButtonListeners() {
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