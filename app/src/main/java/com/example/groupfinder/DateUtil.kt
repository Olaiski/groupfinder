package com.example.groupfinder

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.groupfinder.database.models.Reservation
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


@SuppressLint("SimpleDateFormat")
fun convertLongToDateString(systemTime: Long): String {
    return SimpleDateFormat("EEEE MMM-dd-yyyy' Time: 'HH:mm")
        .format(systemTime).toString()
}

// TODO: 21/09/2020 Ta bort, eller bruk på en annen måte.. Bare dummy
fun formatReservations(reservations: ArrayList<com.example.groupfinder.calendar.Reservation>, resource: Resources) : Spanned {

    val sb = StringBuilder()

    sb.apply {
        reservations.forEach {
            append("<b>Groupname:</b>")
            append("\t${it.groupName}<br>")
            append("<b>Start:</b>")
            append("\t${it.startTime} <b> End:</b>${it.endTime} </b>")
            append("<b>Date:</b>")
            append("\t${it.date}<br>")
            append("<b>Location:</b>")
            append("\t${it.location}<br>")
            append("<b>Room:</b>")
            append("\t${it.roomNumber}<br><br>")


        }
    }
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(sb.toString(), Html.FROM_HTML_MODE_LEGACY)
    } else {
        HtmlCompat.fromHtml(sb.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
}

// Dummy
fun formatGroup(groups: ArrayList<com.example.groupfinder.userprofile.Group>, resource: Resources) : Spanned {

    val sb = StringBuilder()

    sb.apply {
        groups.forEach {
            append("<b>${it.courseCode}<b><br>")
            append("\t${it.title}<br>")
            append("\t${it.description}<br><br>")
        }
    }
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(sb.toString(), Html.FROM_HTML_MODE_LEGACY)
    } else {
        HtmlCompat.fromHtml(sb.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
}


/**
 *  Formats date from the datepicker
 */
fun onDateSet(dateString: String) : String{

    val targetFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
    val originalFormat = SimpleDateFormat("MMM dd, yyy", Locale.ENGLISH)
    val date : Date = originalFormat.parse(dateString)!!

    return targetFormat.format(date)
}



class TextItemViewHolder(val textView: TextView): RecyclerView.ViewHolder(textView)