package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val buttonSettingsBack = findViewById<Button>(R.id.button_settings_back)

        buttonSettingsBack.setOnClickListener {
            val settingsBackIntent = Intent(this, MainActivity::class.java)
            startActivity(settingsBackIntent)
        }
    }
}