package com.example.groupfinder.network.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 *  Modeller for API / JSON
 */
@Parcelize
data class Reservation(
    val startDateTime: String,
    val endDateTime: String,
    val roomId: Int?,
    val GroupId: Int?
): Parcelable

@Parcelize
data class UserReservation(
    val rId: Int,
    val groupName: String,
    val date: String,
    val start: String,
    val end: String,
    val roomName: String,
    val location: String
): Parcelable

@Parcelize
data class UserReservations(
    val message: String,
    val userReservations: List<UserReservation>
): Parcelable