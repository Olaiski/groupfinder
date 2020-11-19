package com.example.groupfinder.network.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Reservation(

    val startDateTime: String,
    val endDateTime: String,
    val roomId: Int?,
    val GroupId: Int?

): Parcelable