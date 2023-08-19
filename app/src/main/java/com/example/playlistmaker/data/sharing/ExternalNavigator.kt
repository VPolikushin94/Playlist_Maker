package com.example.playlistmaker.data.sharing

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.domain.sharing.models.EmailData

class ExternalNavigator(private val context: Context) {

    fun shareApp(url: String) {
        val sendIntent: Intent = Intent().apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, url)
            type = "text/plain"
        }
        startActivity(sendIntent)
    }

    fun openLicence(url: String) {
        val licenceIntent = Intent().apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            action = Intent.ACTION_VIEW
            data = Uri.parse(url)
        }
        startActivity(licenceIntent)
    }

    fun openSupport(emailData: EmailData) {
        val shareIntent = Intent().apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            action = Intent.ACTION_SENDTO
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, emailData.email)
            putExtra(Intent.EXTRA_SUBJECT, emailData.subject)
            putExtra(Intent.EXTRA_TEXT, emailData.text)
        }
        startActivity(shareIntent)
    }

    private fun startActivity(intent: Intent) {
        try {
            context.startActivity(intent)
        } catch (e: Throwable){
            return
        }
    }
}