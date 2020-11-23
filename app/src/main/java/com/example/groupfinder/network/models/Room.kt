package com.example.groupfinder.network.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 *  Modeller for API / JSON
 */
@Parcelize
data class VacantRooms(
    val message: String,
    val vacantRooms: List<Room>
) : Parcelable

@Parcelize
data class Room(
    val id: Int,
    val name: String
) : Parcelable