package com.example.groupfinder.network.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize


@Parcelize
data class UserGroups(
    val message: String,
    val userGroups: List<Group>,
) : Parcelable

@Parcelize
data class GroupMembers (
    val message: String,
    val groupMembers: List<StudentCompact>
) : Parcelable

@Parcelize
data class Group(

    val sId: Int,
    val groupName: String,
    val description: String,
    val courseCode: String,
    val location: String,
    val gId: Int,
) : Parcelable


@Parcelize
data class GroupLeaderGroups(
    val message: String,
    val groupLeaderGroups: List<GroupLeaderGroup>
) : Parcelable

@Parcelize
data class GroupLeaderGroup(
    val id: Int,
    val groupName: String,
    val description: String
) : Parcelable

@Parcelize
data class PostGroup(

    val studentId: Int,
    val groupName: String,
    val description: String,
    val courseCode: String,
    val location: String

) : Parcelable


@Parcelize
data class PostMessage(
    val message: String
): Parcelable
