package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
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
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchActivity : AppCompatActivity() {

    private lateinit var etSearch: EditText
    private lateinit var buttonClear: ImageButton
    private lateinit var buttonSearchBack: ImageButton
    private var searchText: String = ""
    private lateinit var rvTrackList: RecyclerView
    private lateinit var rvSearchHistory: RecyclerView
    private var trackList = ArrayList<Track>()
    private var historyTrackList = ArrayList<Track>()
    private lateinit var trackListAdapter: TrackListAdapter
    private lateinit var historyTrackListAdapter: HistoryTrackListAdapter
    private lateinit var llPlaceholder: LinearLayout
    private lateinit var buttonPlaceholder: Button
    private lateinit var ivPlaceholder: ImageView
    private lateinit var tvPlaceholder: TextView
    private lateinit var searchHistory: SearchHistory
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var llSearchHistory: LinearLayout
    private lateinit var buttonClearHistory: Button
    private lateinit var sharedPrefsListener: SharedPreferences.OnSharedPreferenceChangeListener


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        setSharedPrefs()
        searchHistory = SearchHistory(sharedPrefs, historyTrackList)

        setViews()
        setRecyclerViewAdapters()
        addTextWatcher()
        setButtonListeners()

        setEditorActionListener()

        setTrackClickListener()

        setEditTextFocusChangeListener()
        etSearch.requestFocus()

        rvSearchHistory.setOnTouchListener { _, _ ->
            hideKeyboard()
            false
        }
    }

    private fun setTrackClickListener() {
        trackListAdapter.onTrackClickListener = {
            searchHistory.addTrackToHistory(it)
        }
        historyTrackListAdapter.onTrackClickListener = {
            searchHistory.addTrackToHistory(it)
        }
    }

    private fun setEditTextFocusChangeListener() {
        etSearch.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && searchText.isEmpty()) {
                showPlaceholder(SearchPlaceholder.PLACEHOLDER_SEARCH_HISTORY)
            } else {
                showPlaceholder(SearchPlaceholder.PLACEHOLDER_HIDDEN)
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
        llSearchHistory = findViewById(R.id.ll_search_history)
        buttonClearHistory = findViewById(R.id.button_clear_history)
    }

    private fun setEditorActionListener() {
        etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search()
                etSearch.clearFocus()
            }
            false
        }
    }

    private fun search() {
        RetrofitInstance.api.search(searchText).enqueue(object : Callback<TracksResponse> {
            override fun onResponse(
                call: Call<TracksResponse>,
                response: Response<TracksResponse>
            ) {
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
                showPlaceholder(SearchPlaceholder.PLACEHOLDER_BAD_CONNECTION)
            }
        })
    }

    private fun showPlaceholder(placeholderType: SearchPlaceholder) {
        when (placeholderType) {
            SearchPlaceholder.PLACEHOLDER_HIDDEN -> {
                llPlaceholder.visibility = View.GONE
                llSearchHistory.visibility = View.GONE
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
                llSearchHistory.visibility = View.VISIBLE
            }
        }
    }

    private fun changePlaceholder(
        isPlaceholderNothingFound: Boolean,
        @DrawableRes image: Int,
        @StringRes text: Int
    ) {
        llSearchHistory.visibility = View.GONE
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
        }
        buttonSearchBack.setOnClickListener {
            finish()
        }
        buttonPlaceholder.setOnClickListener {
            search()
        }
        buttonClearHistory.setOnClickListener{
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

                if(etSearch.hasFocus() && searchText.isEmpty()) {
                    showPlaceholder(SearchPlaceholder.PLACEHOLDER_SEARCH_HISTORY)
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

        const val SHARED_PREFERENCES = "SHARED_PREFERENCES"
    }
}