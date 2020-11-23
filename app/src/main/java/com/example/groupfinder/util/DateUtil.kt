package com.example.groupfinder.util

import java.text.SimpleDateFormat
import java.util.*

/**
 *  Formats date from the datepicker
 */
fun onDateSet(dateString: String) : String{

    val targetFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
    val originalFormat = SimpleDateFormat("MMM dd, yyy", Locale.ENGLISH)
    val date : Date = originalFormat.parse(dateString)!!

    return targetFormat.format(date)
}
