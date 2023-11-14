package com.example.playlistmaker.data.sharing

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.domain.sharing.models.EmailData

class ExternalNavigator(private val context: Context) {

    fun shareApp(url: String): Boolean {
        val sendIntent: Intent = Intent().apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, url)
            type = "text/plain"
        }
        return startActivity(sendIntent)
    }

    fun sharePlaylist(message: String): Boolean {
        val sendIntent: Intent = Intent().apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, message)
            type = "text/plain"
        }
        return startActivity(sendIntent)
    }

    fun openLicence(url: String): Boolean {
        val licenceIntent = Intent().apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            action = Intent.ACTION_VIEW
            data = Uri.parse(url)
        }
        return startActivity(licenceIntent)
    }

    fun openSupport(emailData: EmailData): Boolean {
        val shareIntent = Intent().apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            action = Intent.ACTION_SENDTO
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, emailData.email)
            putExtra(Intent.EXTRA_SUBJECT, emailData.subject)
            putExtra(Intent.EXTRA_TEXT, emailData.text)
        }
        return startActivity(shareIntent)
    }

    private fun startActivity(intent: Intent): Boolean {
        return try {
            context.startActivity(intent)
            true
        } catch (e: Throwable) {
            false
        }
    }
}