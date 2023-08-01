package com.example.playlistmaker.ui.search

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.Creator
import com.example.playlistmaker.ui.search.adapter.HistoryTrackListAdapter
import com.example.playlistmaker.R
import com.example.playlistmaker.presentation.models.SearchPlaceholderState
import com.example.playlistmaker.ui.search.adapter.TrackListAdapter
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.api.search.SearchPresenter
import com.example.playlistmaker.presentation.api.search.SearchView
import com.example.playlistmaker.presentation.impl.SearchPresenterImpl
import com.example.playlistmaker.ui.player.AudioPlayerActivity


class SearchActivity : AppCompatActivity(), SearchView {

    private lateinit var etSearch: EditText
    private lateinit var buttonClear: ImageButton
    private lateinit var buttonSearchBack: ImageButton
    private lateinit var rvTrackList: RecyclerView
    private lateinit var rvSearchHistory: RecyclerView
    private lateinit var trackListAdapter: TrackListAdapter
    private lateinit var historyTrackListAdapter: HistoryTrackListAdapter
    private lateinit var llPlaceholder: LinearLayout
    private lateinit var buttonPlaceholder: Button
    private lateinit var ivPlaceholder: ImageView
    private lateinit var tvPlaceholder: TextView

    private lateinit var nsvSearchHistory: NestedScrollView
    private lateinit var buttonClearHistory: Button
    private lateinit var flSearchContent: FrameLayout
    private lateinit var pbSearch: ProgressBar

    private lateinit var searchPresenter: SearchPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchPresenter = SearchPresenterImpl(Creator.searchInteractor, this)

        setViews()
        setRecyclerViewAdapters()
        addTextWatcher()
        setButtonListeners()

        setEditorActionListener()

        setTrackClickListener()

        setEditTextFocusChangeListener()
        etSearch.requestFocus()

        setListTouchListeners()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setListTouchListeners() {
        nsvSearchHistory.setOnTouchListener { _, _ ->
            hideKeyboard()
            false
        }
        rvTrackList.setOnTouchListener { _, _ ->
            hideKeyboard()
            etSearch.clearFocus()
            false
        }
    }

    private fun setTrackClickListener() {
        trackListAdapter.onTrackClickListener = {
            clickTrack(it)
        }
        historyTrackListAdapter.onTrackClickListener = {
            clickTrack(it)
        }
    }

    private fun clickTrack(track: Track) {
        if (searchPresenter.clickDebounce()) {
            searchPresenter.addTrackToHistory(track)
            val playerActivity = Intent(this, AudioPlayerActivity::class.java)
            playerActivity.putExtra(TRACK_INFO, track)
            startActivity(playerActivity)
        }
    }


    private fun setEditTextFocusChangeListener() {
        etSearch.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && searchPresenter.searchText.isEmpty()) {
                if (historyTrackListAdapter.historyTrackList.isNotEmpty()) {
                    showPlaceholder(SearchPlaceholderState.PLACEHOLDER_SEARCH_HISTORY)
                } else {
                    showPlaceholder(SearchPlaceholderState.PLACEHOLDER_HIDDEN)
                }
            } else {
                if (searchPresenter.searchText.isEmpty()) {
                    showPlaceholder(SearchPlaceholderState.PLACEHOLDER_HIDDEN)
                }
            }
        }
    }

    private fun setViews() {
        etSearch = findViewById(R.id.et_search)
        buttonClear = findViewById(R.id.button_clear)
        buttonSearchBack = findViewById(R.id.button_search_back)
        llPlaceholder = findViewById(R.id.ll_placeholder)
        ivPlaceholder = findViewById(R.id.iv_placeholder)
        tvPlaceholder = findViewById(R.id.tv_placeholder)
        buttonPlaceholder = findViewById(R.id.button_placeholder)
        rvTrackList = findViewById(R.id.rv_track_list)
        rvSearchHistory = findViewById(R.id.rv_search_history)
        nsvSearchHistory = findViewById(R.id.nsv_search_history)
        buttonClearHistory = findViewById(R.id.button_clear_history)
        flSearchContent = findViewById(R.id.fl_search_content)
        pbSearch = findViewById(R.id.pb_search)
    }

    private fun setEditorActionListener() {
        etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchPresenter.searchTrack()
                etSearch.clearFocus()
            }
            false
        }
    }


    override fun showProgressBar(isVisible: Boolean) {
        if (isVisible) {
            flSearchContent.isVisible = false
            pbSearch.isVisible = true
        } else {
            pbSearch.isVisible = false
            flSearchContent.isVisible = true
        }
    }


    private fun showPlaceholder(placeholderType: SearchPlaceholderState) {
        when (placeholderType) {
            SearchPlaceholderState.PLACEHOLDER_HIDDEN -> {
                llPlaceholder.visibility = View.GONE
                nsvSearchHistory.visibility = View.GONE
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
                clearTrackList()
                llPlaceholder.visibility = View.GONE
                nsvSearchHistory.visibility = View.VISIBLE
            }
        }
    }

    private fun changePlaceholder(
        isPlaceholderNothingFound: Boolean,
        @DrawableRes image: Int,
        @StringRes text: Int
    ) {
        nsvSearchHistory.visibility = View.GONE
        clearTrackList()

        ivPlaceholder.setImageResource(image)
        tvPlaceholder.text = getString(text)

        buttonPlaceholder.isVisible = !isPlaceholderNothingFound
        llPlaceholder.visibility = View.VISIBLE
    }

    private fun setRecyclerViewAdapters() {
        trackListAdapter = TrackListAdapter()
        trackListAdapter.trackList = searchPresenter.trackList
        rvTrackList.adapter = trackListAdapter

        historyTrackListAdapter = HistoryTrackListAdapter()
        historyTrackListAdapter.historyTrackList = searchPresenter.getHistoryTrackList()
        rvSearchHistory.adapter = historyTrackListAdapter
    }

    private fun setButtonListeners() {
        buttonClear.setOnClickListener {
            hideKeyboard()
            etSearch.setText("")
            clearTrackList()
            etSearch.requestFocus()
        }
        buttonSearchBack.setOnClickListener {
            finish()
        }
        buttonPlaceholder.setOnClickListener {
            searchPresenter.searchTrack()
        }
        buttonClearHistory.setOnClickListener {
            showPlaceholder(SearchPlaceholderState.PLACEHOLDER_HIDDEN)
            searchPresenter.clearHistoryTrackList()
        }
    }

    private fun clearTrackList() {
        searchPresenter.trackList.clear()
        trackListAdapter.notifyDataSetChanged()
    }

    private fun addTextWatcher() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                buttonClear.visibility = clearButtonVisibility(s)
                searchPresenter.searchText = s.toString()

                searchPresenter.searchDebounce()

                if (etSearch.hasFocus() && searchPresenter.searchText.isEmpty()) {
                    if (historyTrackListAdapter.historyTrackList.isNotEmpty()) {
                        showPlaceholder(SearchPlaceholderState.PLACEHOLDER_SEARCH_HISTORY)
                    } else {
                        showPlaceholder(SearchPlaceholderState.PLACEHOLDER_HIDDEN)
                    }
                } else {
                    showPlaceholder(SearchPlaceholderState.PLACEHOLDER_HIDDEN)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        }

        etSearch.addTextChangedListener(textWatcher)
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun hideKeyboard() {
        val keyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        keyboard.hideSoftInputFromWindow(etSearch.windowToken, 0)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, searchPresenter.searchText)
        outState.putParcelableArrayList(TRACK_LIST, searchPresenter.trackList)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchPresenter.searchText = savedInstanceState.getString(SEARCH_TEXT)!!
        etSearch.setText(searchPresenter.searchText)
        savedInstanceState.getParcelableArrayList<Track>(TRACK_LIST)?.let { searchPresenter.trackList.addAll(it) }
        trackListAdapter.notifyDataSetChanged()
    }

    override fun showSearchResult(
        placeholderState: SearchPlaceholderState,
        trackList: List<Track>
    ) {
        when (placeholderState) {
            SearchPlaceholderState.PLACEHOLDER_HIDDEN ->
                showPlaceholder(SearchPlaceholderState.PLACEHOLDER_HIDDEN)
            SearchPlaceholderState.PLACEHOLDER_NOTHING_FOUND ->
                showPlaceholder(SearchPlaceholderState.PLACEHOLDER_NOTHING_FOUND)
            else -> showPlaceholder(SearchPlaceholderState.PLACEHOLDER_BAD_CONNECTION)
        }

        trackListAdapter.notifyDataSetChanged()
    }

    override fun updateHistoryTrackList() {
        historyTrackListAdapter.notifyDataSetChanged()
    }

    companion object {
        private const val SEARCH_TEXT = "SEARCH_TEXT"
        private const val TRACK_LIST = "TRACK_LIST"
        const val TRACK_INFO = "TRACK_INFO"
    }
}