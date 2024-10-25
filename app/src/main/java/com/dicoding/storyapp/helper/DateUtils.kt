package com.dicoding.storyapp.helper

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    // Function to localize the date to Indonesia Time (WIB)
    fun localizeDate(utcDate: String): String {
        return try {
            // Parse the original UTC date
            val utcFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            utcFormat.timeZone = TimeZone.getTimeZone("UTC")
            val date: Date? = utcFormat.parse(utcDate)

            // Convert to local time (WIB - Indonesia)
            val localFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            localFormat.timeZone = TimeZone.getTimeZone("Asia/Jakarta")

            // Return the formatted local date
            localFormat.format(date ?: return utcDate)
        } catch (e: Exception) {
            e.printStackTrace()
            utcDate  // Return the original UTC date if there's an error
        }
    }
}
