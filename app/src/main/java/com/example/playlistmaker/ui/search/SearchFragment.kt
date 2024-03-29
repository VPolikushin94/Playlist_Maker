package com.example.playlistmaker.ui.search

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.ui.player.AudioPlayerFragment
import com.example.playlistmaker.ui.search.adapter.HistoryTrackListAdapter
import com.example.playlistmaker.ui.search.adapter.TrackListAdapter
import com.example.playlistmaker.ui.search.models.SearchPlaceholderState
import com.example.playlistmaker.ui.search.models.SearchScreenState
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private var trackListAdapter: TrackListAdapter? = null
    private var historyTrackListAdapter: HistoryTrackListAdapter? = null

    private val searchViewModel: SearchViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerViewAdapters()

        if (savedInstanceState != null) {
            restoreInstanceState(savedInstanceState)
        }

        addTextWatcher()
        setButtonListeners()

        setEditorActionListener()

        setTrackClickListener()

        setEditTextFocusChangeListener()
        binding.etSearch.requestFocus()

        setListTouchListeners()

        searchViewModel.searchScreenState.observe(viewLifecycleOwner) {
            render(it)
        }

    }

    override fun onStart() {
        super.onStart()
        if (searchViewModel.searchText.isNotEmpty()) {
            searchViewModel.updateSearchTrackList()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.rvTrackList.adapter = null
        trackListAdapter = null

        binding.rvSearchHistory.adapter = null
        historyTrackListAdapter = null
        _binding = null
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setListTouchListeners() {
        binding.nsvSearchHistory.setOnTouchListener { _, _ ->
            hideKeyboard()
            false
        }
        binding.rvTrackList.setOnTouchListener { _, _ ->
            hideKeyboard()
            binding.etSearch.clearFocus()
            false
        }
    }

    private fun setTrackClickListener() {
        trackListAdapter?.onTrackClickListener = {
            clickTrack(it)
        }
        historyTrackListAdapter?.onTrackClickListener = {
            clickTrack(it)
        }
    }

    private fun clickTrack(track: Track) {
        if (searchViewModel.clickDebounce()) {
            searchViewModel.addTrackToHistory(track)
            findNavController().navigate(
                R.id.action_searchFragment_to_audioPlayerFragment,
                AudioPlayerFragment.getAudioPlayerBundle(track)
            )
        }
    }

    private fun setEditTextFocusChangeListener() {
        binding.etSearch.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && searchViewModel.searchText.isEmpty()) {
                if (!historyTrackListAdapter?.currentList.isNullOrEmpty()) {
                    showPlaceholder(SearchPlaceholderState.PLACEHOLDER_SEARCH_HISTORY)
                } else {
                    showPlaceholder(SearchPlaceholderState.PLACEHOLDER_HIDDEN)
                }
            }
        }
    }

    private fun setEditorActionListener() {
        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchViewModel.searchTrack(true)
                binding.etSearch.clearFocus()
            }
            false
        }
    }

    private fun showProgressBar(isVisible: Boolean) {
        if (isVisible) {
            binding.flSearchContent.isVisible = false
            binding.pbSearch.isVisible = true
        } else {
            binding.pbSearch.isVisible = false
            binding.flSearchContent.isVisible = true
        }
    }

    private fun showPlaceholder(placeholderType: SearchPlaceholderState) {
        when (placeholderType) {
            SearchPlaceholderState.PLACEHOLDER_HIDDEN -> {
                binding.llPlaceholder.visibility = View.GONE
                binding.nsvSearchHistory.visibility = View.GONE
            }

            SearchPlaceholderState.PLACEHOLDER_NOTHING_FOUND -> {
                changePlaceholder(
                    true,
                    R.drawable.ic_nothing_found_placeholder,
                    R.string.nothing_found
                )
            }

            SearchPlaceholderState.PLACEHOLDER_BAD_CONNECTION -> {
                changePlaceholder(
                    false,
                    R.drawable.ic_bad_connection_placeholder,
                    R.string.bad_connection
                )
            }

            SearchPlaceholderState.PLACEHOLDER_SEARCH_HISTORY -> {
                binding.llPlaceholder.visibility = View.GONE
                binding.nsvSearchHistory.visibility = View.VISIBLE
            }
        }
    }

    private fun changePlaceholder(
        isPlaceholderNothingFound: Boolean,
        @DrawableRes image: Int,
        @StringRes text: Int,
    ) {
        binding.nsvSearchHistory.visibility = View.GONE
        clearTrackList()

        binding.ivPlaceholder.setImageResource(image)
        binding.tvPlaceholder.text = getString(text)

        binding.buttonPlaceholder.isVisible = !isPlaceholderNothingFound
        binding.llPlaceholder.visibility = View.VISIBLE
    }

    private fun setRecyclerViewAdapters() {
        trackListAdapter = TrackListAdapter(false)
        binding.rvTrackList.itemAnimator = null
        binding.rvTrackList.adapter = trackListAdapter

        historyTrackListAdapter = HistoryTrackListAdapter()
        binding.rvSearchHistory.itemAnimator = null
        binding.rvSearchHistory.adapter = historyTrackListAdapter
    }

    private fun setButtonListeners() {
        binding.buttonClear.setOnClickListener {
            hideKeyboard()
            binding.etSearch.setText("")
            searchViewModel.searchText = ""
            clearTrackList()
            binding.etSearch.requestFocus()
        }
        binding.buttonPlaceholder.setOnClickListener {
            searchViewModel.searchTrack(false)
        }
        binding.buttonClearHistory.setOnClickListener {
            showPlaceholder(SearchPlaceholderState.PLACEHOLDER_HIDDEN)
            searchViewModel.clearHistoryTrackList()
        }
    }

    private fun clearTrackList() {
        trackListAdapter?.submitList(null)
    }

    private fun addTextWatcher() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.buttonClear.visibility = clearButtonVisibility(s)
                searchViewModel.searchText = s.toString()

                if(searchViewModel.shouldSearch) {
                    searchViewModel.searchDebounce()
                } else {
                    searchViewModel.shouldSearch = true
                }

                if (binding.etSearch.hasFocus() && searchViewModel.searchText.isEmpty()) {
                    searchViewModel.getHistoryTrackList()

                    showPlaceholder(SearchPlaceholderState.PLACEHOLDER_HIDDEN)
                } else {
                    showPlaceholder(SearchPlaceholderState.PLACEHOLDER_HIDDEN)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        }

        binding.etSearch.addTextChangedListener(textWatcher)
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    override fun onStop() {
        super.onStop()
        searchViewModel.shouldSearch = false
    }

    private fun hideKeyboard() {
        val keyboard =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        keyboard.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, searchViewModel.searchText)
    }

    private fun restoreInstanceState(savedInstanceState: Bundle) {
        searchViewModel.searchText = savedInstanceState.getString(SEARCH_TEXT)!!
        binding.etSearch.setText(searchViewModel.searchText)
    }

    private fun render(state: SearchScreenState) {
        when (state) {
            is SearchScreenState.Content -> {
                showProgressBar(false)
                if (state.isEmpty) {
                    clearTrackList()
                    showPlaceholder(SearchPlaceholderState.PLACEHOLDER_NOTHING_FOUND)
                } else {
                    showPlaceholder(SearchPlaceholderState.PLACEHOLDER_HIDDEN)
                    trackListAdapter?.submitList(state.trackList)
                }
            }

            is SearchScreenState.Error -> {
                showProgressBar(false)
                showPlaceholder(SearchPlaceholderState.PLACEHOLDER_BAD_CONNECTION)
            }

            is SearchScreenState.Loading -> {
                showProgressBar(true)
                showPlaceholder(SearchPlaceholderState.PLACEHOLDER_HIDDEN)
            }
            is SearchScreenState.HistoryContent -> {
                showProgressBar(false)
                showPlaceholder(SearchPlaceholderState.PLACEHOLDER_SEARCH_HISTORY)
                clearTrackList()
                historyTrackListAdapter?.submitList(state.trackList)
            }
        }
    }

    companion object {
        private const val SEARCH_TEXT = "SEARCH_TEXT"
    }

}