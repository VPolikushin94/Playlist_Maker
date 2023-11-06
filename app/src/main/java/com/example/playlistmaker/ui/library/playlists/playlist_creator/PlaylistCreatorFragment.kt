package com.example.playlistmaker.ui.library.playlists.playlist_creator

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistCreatorBinding
import com.example.playlistmaker.ui.library.playlists.playlist_creator.models.PlaylistCreatingState
import com.example.playlistmaker.ui.library.playlists.playlist_creator.view_model.PlaylistCreatorViewModel
import com.example.playlistmaker.util.ImageSaver
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlaylistCreatorFragment : Fragment() {

    private var _binding: FragmentPlaylistCreatorBinding? = null
    private val binding get() = _binding!!

    private val playlistCreatorViewModel: PlaylistCreatorViewModel by viewModel()

    private lateinit var photoPicker: ActivityResultLauncher<PickVisualMediaRequest>

    private lateinit var confirmDialog: MaterialAlertDialogBuilder

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            exitChecker()
        }
    }

    private var playlistName: String = ""
    private var playlistDescription: String = ""
    private var playlistImageUri: Uri? = Uri.EMPTY

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPlaylistCreatorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addTextWatchers()
        setClickListeners()
        setListTouchListeners()
        setEditorActionListeners()
        setPhotoPicker()
        setConfirmDialog()

        if (savedInstanceState != null) {
            restoreInstanceState(savedInstanceState)
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)

        playlistCreatorViewModel.playlistCreatingState.observe(viewLifecycleOwner) {
            when(it) {
                is PlaylistCreatingState.Created -> {
                    showSnackBar()
                    findNavController().navigateUp()
                }
                else -> {}
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setConfirmDialog() {
        confirmDialog = MaterialAlertDialogBuilder(requireContext(), R.style.MyAlertDialog)
            .setTitle(getString(R.string.confirm_dialog_title))
            .setMessage(getString(R.string.confirm_dialog_message))
            .setNegativeButton(getString(R.string.confirm_dialog_negative)) { dialog, _ ->
                dialog.cancel()
            }
            .setPositiveButton(getString(R.string.confirm_dialog_positive)) { _, _ ->
                findNavController().navigateUp()
            }

    }

    private fun isAnyData() = playlistImageUri != Uri.EMPTY ||
            playlistName.isNotEmpty() ||
            playlistDescription.isNotEmpty()

    private fun exitChecker() {
        if (isAnyData()) {
            confirmDialog.show()
        } else {
            findNavController().navigateUp()
        }
    }

    private fun setPhotoPicker() {
        photoPicker =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                uri?.let {
                    setImage(uri)
                    playlistImageUri = uri
                }
            }
    }


    private fun setImage(uri: Uri) {
        Glide.with(this)
            .load(uri)
            .centerCrop()
            .transform(
                RoundedCorners(
                    requireContext().resources.getDimensionPixelSize(R.dimen.search_grid_album_radius)
                )
            )
            .into(binding.ivAddImage)
    }

    private fun addTextWatchers() {
        val nameTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                playlistName = s.toString()

                if (s.isNullOrEmpty()) {
                    binding.buttonCreate.isEnabled = false
                    binding.buttonCreate.setBackgroundColor(requireContext().getColor(R.color.button_create_disabled))
                } else {
                    binding.buttonCreate.isEnabled = true
                    binding.buttonCreate.setBackgroundColor(requireContext().getColor(R.color.button_create_enabled))
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        }
        binding.etName.addTextChangedListener(nameTextWatcher)

        val descriptionTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                playlistDescription = s.toString()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        }
        binding.etDescription.addTextChangedListener(descriptionTextWatcher)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setListTouchListeners() {
        binding.root.setOnTouchListener { _, _ ->
            hideKeyboard()
            binding.etName.clearFocus()
            binding.etDescription.clearFocus()
            false
        }
    }

    private fun setClickListeners() {
        binding.buttonPlaylistCreatorBack.setOnClickListener {
            exitChecker()
        }
        binding.ivAddImage.setOnClickListener {
            photoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        binding.buttonCreate.setOnClickListener {
            if(playlistCreatorViewModel.clickDebounce()) {
                var savedImgName = ""
                if (playlistImageUri != Uri.EMPTY) {
                    playlistImageUri?.let {
                        savedImgName = ImageSaver.saveImage(
                            requireActivity(),
                            it,
                            playlistName + IMAGE_FORMAT)
                    }
                }
                playlistCreatorViewModel.addPlaylist(
                    playlistName,
                    savedImgName,
                    playlistDescription
                )
            }
        }
    }

    private fun showSnackBar() {
        Snackbar.make(
            binding.root,
            "Плейлист $playlistName создан",
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun setEditorActionListeners() {
        binding.etName.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.etName.clearFocus()
            }
            false
        }

        binding.etDescription.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.etDescription.clearFocus()
            }
            false
        }
    }

    private fun hideKeyboard() {
        val keyboard =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        keyboard.hideSoftInputFromWindow(binding.etName.windowToken, 0)
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(IMAGE_URI, playlistImageUri)
        outState.putString(NAME, playlistName)
        outState.putString(DESCRIPTION, playlistDescription)
    }

    private fun restoreInstanceState(savedInstanceState: Bundle) {
        playlistImageUri = savedInstanceState.getParcelable(IMAGE_URI)
        if(playlistImageUri != Uri.EMPTY) {
            playlistImageUri?.let { setImage(it) }
        }

        playlistName = savedInstanceState.getString(NAME, "")
        playlistDescription = savedInstanceState.getString(DESCRIPTION, "")
    }

    companion object {
        const val ALBUM_PICTURES_DIR = "album_pictures"
        private const val IMAGE_FORMAT = ".jpg"

        private const val IMAGE_URI = "IMAGE_URI"
        private const val NAME = "NAME"
        private const val DESCRIPTION = "DESCRIPTION"
    }

}