package com.example.playlistmaker.ui.settings.models

sealed interface ShareState{
    object Success : ShareState
    data class Error(val shareType: ShareType) : ShareState
}



