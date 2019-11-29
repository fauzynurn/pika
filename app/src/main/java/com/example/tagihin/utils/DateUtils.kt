package com.example.tagihin.utils

import java.text.SimpleDateFormat
import java.util.*

class DateUtils{
    companion object{
        fun getCurrentDate(format : String) : String {
            val dateFormat = SimpleDateFormat(format)
            val today: Date = Calendar.getInstance().getTime()
            return dateFormat.format(today)
        }

        fun formatDate(format : String, date : String) : String{
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS",Locale.ENGLISH)
            val outputFormat = SimpleDateFormat(format, Locale.ENGLISH)
            val finalDate: Date = inputFormat.parse(date)!!
            return outputFormat.format(finalDate)
        }
    }
}