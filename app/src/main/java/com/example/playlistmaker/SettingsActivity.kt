package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class SettingsActivity : AppCompatActivity() {

    private lateinit var buttonSettingsBack: Button
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
                putExtra(Intent.EXTRA_TEXT, "https://practicum.yandex.ru/android-developer/")
                type = "text/plain"
            }
            startActivity(sendIntent)
        }

        buttonSupport.setOnClickListener {
            val subject = "Сообщение разработчикам и разработчицам приложения Playlist Maker"
            val message = "Спасибо разработчикам и разработчицам за крутое приложение!"
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SENDTO
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf("dj0j@yandex.ru"))
                putExtra(Intent.EXTRA_SUBJECT, subject)
                putExtra(Intent.EXTRA_TEXT, message)
            }
            startActivity(shareIntent)
        }

        buttonLicence.setOnClickListener {
            val licenceIntent = Intent().apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse("https://yandex.ru/legal/practicum_offer/")
            }
            startActivity(licenceIntent)
        }
    }
}