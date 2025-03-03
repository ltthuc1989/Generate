package com.ltthuc.feature.utils

import com.ltthuc.ui.utils.TimeUtils
import java.text.SimpleDateFormat
import java.util.TimeZone

object TimeUtils {
    fun formatTime(timeStamp: String, format: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

        val date = dateFormat.parse(timeStamp)
        val timeFormat = SimpleDateFormat(format)

        return timeFormat.format(date)
    }
}