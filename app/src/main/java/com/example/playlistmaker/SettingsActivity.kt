package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class SettingsActivity : AppCompatActivity() {

    private lateinit var buttonSettingsBack: ImageButton
    private lateinit var buttonShare: Button
    private lateinit var buttonSupport: Button
    private lateinit var buttonLicence: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        setButtons()
        setButtonListeners()
    }

    private fun setButtons() {
        buttonSettingsBack = findViewById(R.id.button_settings_back)
        buttonShare = findViewById(R.id.button_share)
        buttonSupport = findViewById(R.id.button_support)
        buttonLicence = findViewById(R.id.button_licence)
    }

    private fun setButtonListeners() {
        buttonSettingsBack.setOnClickListener {
            finish()
        }

        buttonShare.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, getString(R.string.share_url))
                type = "text/plain"
            }
            startActivity(sendIntent)
        }

        buttonSupport.setOnClickListener {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SENDTO
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.email_rec)))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.email_message))
            }
            startActivity(shareIntent)
        }

        buttonLicence.setOnClickListener {
            val licenceIntent = Intent().apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse(getString(R.string.licence_url))
            }
            startActivity(licenceIntent)
        }
    }
}