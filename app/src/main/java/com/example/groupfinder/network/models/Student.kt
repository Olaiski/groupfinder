package com.example.groupfinder.network.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class ResponseStudent (
    val message: String,
    val student: Student

) : Parcelable


@Parcelize
data class Student(
    val id: Int,
    val firstname: String,
    val lastname: String,
    val email: String,
    val phonenumber: Int

) : Parcelable

@Parcelize
data class StudentCompact(
    val id: Int,
    val firstname: String,
    val lastname: String
) : Parcelable