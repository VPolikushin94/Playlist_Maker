package com.example.playlistmaker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton

class SearchActivity : AppCompatActivity() {

    private lateinit var etSearch: EditText
    private lateinit var buttonClear: ImageButton
    private lateinit var buttonSearchBack: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        setButtons()
        addTextWatcher()
        setButtonListeners()
    }

    private fun setButtonListeners() {
        buttonClear.setOnClickListener {
            hideKeyboard()
            etSearch.setText("")
        }
        buttonSearchBack.setOnClickListener {
            finish()
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
        outState.putString(SEARCH_TEXT, etSearch.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        etSearch.setText(savedInstanceState.getString(SEARCH_TEXT))
    }

    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
    }
}