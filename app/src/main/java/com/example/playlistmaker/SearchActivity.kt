package com.example.playlistmaker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
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
    private var trackList = ArrayList<Track>()
    private lateinit var trackListAdapter: TrackListAdapter
    private lateinit var llPlaceholder: LinearLayout
    private lateinit var buttonPlaceholder: Button
    private lateinit var ivPlaceholder: ImageView
    private lateinit var tvPlaceholder: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        setRecyclerView()
        setButtons()
        setPlaceholderViews()
        addTextWatcher()
        setButtonListeners()

        setEditorActionListener()
    }

    private fun setPlaceholderViews() {
        llPlaceholder = findViewById(R.id.ll_placeholder)
        ivPlaceholder = findViewById(R.id.iv_placeholder)
        tvPlaceholder = findViewById(R.id.tv_placeholder)
        buttonPlaceholder = findViewById(R.id.button_placeholder)
    }

    private fun setEditorActionListener() {
        etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search()
                true
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
                        showPlaceholder(PLACEHOLDER_NOTHING_FOUND)
                    } else {
                        showPlaceholder(PLACEHOLDER_HIDDEN)
                    }
                } else {
                    showPlaceholder(PLACEHOLDER_BAD_CONNECTION)
                }
            }

            override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                showPlaceholder(PLACEHOLDER_BAD_CONNECTION)
            }

        })
    }

    private fun showPlaceholder(placeholderType: Int) {
        when (placeholderType) {
            PLACEHOLDER_HIDDEN -> llPlaceholder.visibility = View.GONE
            PLACEHOLDER_NOTHING_FOUND -> {
                changePlaceholder(
                    true,
                    R.drawable.ic_nothing_found_placeholder,
                    R.string.nothing_found
                )
            }
            PLACEHOLDER_BAD_CONNECTION -> {
                changePlaceholder(
                    false,
                    R.drawable.ic_bad_connection_placeholder,
                    R.string.bad_connection
                )
            }
        }
    }

    private fun changePlaceholder(
        isPlaceholderNothingFound: Boolean,
        @DrawableRes image: Int,
        @StringRes text: Int
    ) {
        trackList.clear()
        trackListAdapter.notifyDataSetChanged()

        ivPlaceholder.setImageResource(image)
        tvPlaceholder.text = getString(text)
        if (isPlaceholderNothingFound) {
            buttonPlaceholder.visibility = View.GONE
        } else {
            buttonPlaceholder.visibility = View.VISIBLE
        }
        llPlaceholder.visibility = View.VISIBLE
    }

    private fun setRecyclerView() {
        rvTrackList = findViewById(R.id.rv_track_list)
        trackListAdapter = TrackListAdapter()
        trackListAdapter.trackList = trackList
        rvTrackList.adapter = trackListAdapter
    }

    private fun setButtonListeners() {
        buttonClear.setOnClickListener {
            hideKeyboard()
            etSearch.setText("")

            trackList.clear()
            trackListAdapter.notifyDataSetChanged()
        }
        buttonSearchBack.setOnClickListener {
            finish()
        }
        buttonPlaceholder.setOnClickListener {
            search()
        }
    }

    private fun setButtons() {
        etSearch = findViewById(R.id.et_search)
        buttonClear = findViewById(R.id.button_clear)
        buttonSearchBack = findViewById(R.id.button_search_back)
    }

    private fun addTextWatcher() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                buttonClear.visibility = clearButtonVisibility(s)
                searchText = s.toString()
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
        etSearch.clearFocus()
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
        const val SEARCH_TEXT = "SEARCH_TEXT"
        const val TRACK_LIST = "TRACK_LIST"

        const val PLACEHOLDER_HIDDEN = 0
        const val PLACEHOLDER_NOTHING_FOUND = 1
        const val PLACEHOLDER_BAD_CONNECTION = 2
    }
}