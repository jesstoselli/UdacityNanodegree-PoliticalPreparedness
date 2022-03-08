package com.example.android.politicalpreparedness.utils

import java.text.SimpleDateFormat
import java.util.*

fun Date.toDateString(): String {
    val calendar = Calendar.getInstance()
    this.let {
        calendar.time = it
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }
}
