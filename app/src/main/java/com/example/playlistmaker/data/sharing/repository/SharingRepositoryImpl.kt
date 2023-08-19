package com.example.playlistmaker.data.sharing.repository

import android.content.Context
import com.example.playlistmaker.R
import com.example.playlistmaker.data.sharing.ExternalNavigator
import com.example.playlistmaker.domain.sharing.api.SharingRepository
import com.example.playlistmaker.domain.sharing.models.EmailData

class SharingRepositoryImpl(
    private val context: Context,
    private val externalNavigator: ExternalNavigator
) : SharingRepository {

    override fun shareApp(): Boolean {
        return externalNavigator.shareApp(getShareAppLink())
    }

    override fun openLicence(): Boolean {
        return externalNavigator.openLicence(getLicenceLink())
    }

    override fun openSupport(): Boolean {
        return externalNavigator.openSupport(getSupportEmailData())
    }

    private fun getShareAppLink(): String {
        return context.getString(R.string.share_url)
    }

    private fun getSupportEmailData(): EmailData {
        return EmailData(
            arrayOf(context.getString(R.string.email_rec)),
            context.getString(R.string.email_subject),
            context.getString(R.string.email_message)
        )
    }

    private fun getLicenceLink(): String {
        return context.getString(R.string.licence_url)
    }
}