package com.example.playlistmaker

import java.text.SimpleDateFormat
import java.util.Locale

object DateTimeUtil {
    fun getFormatTime(time: Int): String =
        SimpleDateFormat("mm:ss", Locale.getDefault()).format(time)
}