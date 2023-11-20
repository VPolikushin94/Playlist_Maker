package com.example.playlistmaker.util

import java.text.SimpleDateFormat
import java.util.Locale

object DateTimeUtil {
    fun getFormatTime(time: Int): String =
        SimpleDateFormat("m:ss", Locale.getDefault()).format(time) ?: "0:00"

    fun getTimeInMilliseconds(time: String): String =
        SimpleDateFormat("m:ss", Locale.getDefault()).parse(time)?.time.toString()

    fun getTimeInMinutes(timeInMilliseconds: Int): Int {
        return SimpleDateFormat("mm", Locale.getDefault()).format(timeInMilliseconds).toInt()
    }
}