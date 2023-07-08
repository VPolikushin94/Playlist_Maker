package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchActivity : AppCompatActivity() {

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
    private lateinit var searchHistory: SearchHistory
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var nsvSearchHistory: NestedScrollView
    private lateinit var buttonClearHistory: Button
    private lateinit var sharedPrefsListener: SharedPreferences.OnSharedPreferenceChangeListener
    private lateinit var flSearchContent: FrameLayout
    private lateinit var pbSearch: ProgressBar
    private var isClickAllowed = true
    private var trackList = ArrayList<Track>()
    private var searchText: String = ""
    private var searchedText: String = ""
    private val handler = Handler(Looper.getMainLooper())

    private val searchRunnable = Runnable { search() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        setSharedPrefs()
        searchHistory = SearchHistory(sharedPrefs)

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
        if(clickDebounce()) {
            searchHistory.addTrackToHistory(track)
            val playerActivity = Intent(this, AudioPlayerActivity::class.java)
            playerActivity.putExtra(TRACK_INFO, track)
            startActivity(playerActivity)
        }
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun setEditTextFocusChangeListener() {
        etSearch.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && searchText.isEmpty()) {
                if(historyTrackListAdapter.historyTrackList.isNotEmpty()) {
                    showPlaceholder(SearchPlaceholder.PLACEHOLDER_SEARCH_HISTORY)
                } else {
                    showPlaceholder(SearchPlaceholder.PLACEHOLDER_HIDDEN)
                }
            } else {
                if(searchText.isEmpty()) {
                    showPlaceholder(SearchPlaceholder.PLACEHOLDER_HIDDEN)
                }
            }
        }
    }

    private fun setSharedPrefs() {
        sharedPrefs = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE)
        sharedPrefsListener =
            SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
                if (key == SearchHistory.SEARCH_HISTORY_KEY) {
                    historyTrackListAdapter.notifyDataSetChanged()
                }
            }
        sharedPrefs.registerOnSharedPreferenceChangeListener(sharedPrefsListener)
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
                handler.removeCallbacks(searchRunnable)
                if(searchedText != searchText) {
                    search()
                }
                etSearch.clearFocus()
            }
            false
        }
    }

    private fun search() {
        if (searchText.isNotEmpty()) {
            searchedText = searchText
            showProgressBar(true)
            RetrofitInstance.api.search(searchText).enqueue(object : Callback<TracksResponse> {
                override fun onResponse(
                    call: Call<TracksResponse>,
                    response: Response<TracksResponse>
                ) {
                    showProgressBar(false)
                    if (response.code() == 200) {
                        trackList.clear()
                        if (response.body()?.trackList?.isNotEmpty() == true) {
                            trackList.addAll(response.body()?.trackList!!)
                            trackListAdapter.notifyDataSetChanged()
                        }
                        if (trackList.isEmpty()) {
                            showPlaceholder(SearchPlaceholder.PLACEHOLDER_NOTHING_FOUND)
                        } else {
                            showPlaceholder(SearchPlaceholder.PLACEHOLDER_HIDDEN)
                        }
                    } else {
                        showPlaceholder(SearchPlaceholder.PLACEHOLDER_BAD_CONNECTION)
                    }
                }

                override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                    showProgressBar(false)
                    showPlaceholder(SearchPlaceholder.PLACEHOLDER_BAD_CONNECTION)
                }
            })
        }
    }

    private fun showProgressBar(isVisible: Boolean) {
        if(isVisible) {
            flSearchContent.isVisible = false
            pbSearch.isVisible = true
        } else {
            pbSearch.isVisible = false
            flSearchContent.isVisible = true
        }
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun showPlaceholder(placeholderType: SearchPlaceholder) {
        when (placeholderType) {
            SearchPlaceholder.PLACEHOLDER_HIDDEN -> {
                llPlaceholder.visibility = View.GONE
                nsvSearchHistory.visibility = View.GONE
            }
            SearchPlaceholder.PLACEHOLDER_NOTHING_FOUND -> {
                changePlaceholder(
                    true,
                    R.drawable.ic_nothing_found_placeholder,
                    R.string.nothing_found
                )
            }
            SearchPlaceholder.PLACEHOLDER_BAD_CONNECTION -> {
                changePlaceholder(
                    false,
                    R.drawable.ic_bad_connection_placeholder,
                    R.string.bad_connection
                )
            }
            SearchPlaceholder.PLACEHOLDER_SEARCH_HISTORY -> {
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
        trackListAdapter.trackList = trackList
        rvTrackList.adapter = trackListAdapter

        historyTrackListAdapter = HistoryTrackListAdapter()
        historyTrackListAdapter.historyTrackList = searchHistory.getHistoryTrackList()
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
            search()
        }
        buttonClearHistory.setOnClickListener{
            showPlaceholder(SearchPlaceholder.PLACEHOLDER_HIDDEN)
            searchHistory.clearHistoryTrackList()
        }
    }

    private fun clearTrackList() {
        trackList.clear()
        trackListAdapter.notifyDataSetChanged()
    }

    private fun addTextWatcher() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                buttonClear.visibility = clearButtonVisibility(s)
                searchText = s.toString()

                searchDebounce()

                if(etSearch.hasFocus() && searchText.isEmpty()) {
                    if(historyTrackListAdapter.historyTrackList.isNotEmpty()) {
                        showPlaceholder(SearchPlaceholder.PLACEHOLDER_SEARCH_HISTORY)
                    } else {
                        showPlaceholder(SearchPlaceholder.PLACEHOLDER_HIDDEN)
                    }
                } else {
                    showPlaceholder(SearchPlaceholder.PLACEHOLDER_HIDDEN)
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
        outState.putString(SEARCH_TEXT, searchText)
        outState.putParcelableArrayList(TRACK_LIST, trackList)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchText = savedInstanceState.getString(SEARCH_TEXT)!!
        etSearch.setText(searchText)
        savedInstanceState.getParcelableArrayList<Track>(TRACK_LIST)?.let { trackList.addAll(it) }
        trackListAdapter.notifyDataSetChanged()
    }

    companion object {
        private const val SEARCH_TEXT = "SEARCH_TEXT"
        private const val TRACK_LIST = "TRACK_LIST"
        const val TRACK_INFO = "TRACK_INFO"

        const val SHARED_PREFERENCES = "SHARED_PREFERENCES"

        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}