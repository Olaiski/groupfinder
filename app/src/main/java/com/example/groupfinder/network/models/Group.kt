package com.example.groupfinder.network.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class UserGroups(
    val message: String,
    val userGroups: List<Group>
) : Parcelable

@Parcelize
data class Group(

    val sId: Int,
    val groupName: String,
    val description: String,
    val courseCode: String,
    val gId: Int

) : Parcelable

