package com.example.groupfinder.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "reservation_table")
data class Reservation(

    @PrimaryKey(autoGenerate = true)
    var reservationId: Long = 0L,

    @ColumnInfo(name = "startTime")
    var startTime: String,

    @ColumnInfo(name = "endTime")
    var endTime: String,

    @ColumnInfo(name = "date")
    var date: String,

    @ColumnInfo(name = "location")
    var location: String,

    @ColumnInfo(name = "roomNumber")
    var roomNumber: Long,

    @ColumnInfo(name = "groupId")
    var groupId: Int,

)