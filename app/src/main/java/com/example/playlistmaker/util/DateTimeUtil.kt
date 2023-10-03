package com.example.playlistmaker.util

import java.text.SimpleDateFormat
import java.util.Locale

object DateTimeUtil {
    fun getFormatTime(time: Int): String =
        SimpleDateFormat("m:ss", Locale.getDefault()).format(time) ?: "0:00"
}