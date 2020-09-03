package com.example.groupfinder.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "group_table")
data class Group(

    @PrimaryKey(autoGenerate = true)
    var groupId: Long = 0L,

    @ColumnInfo(name = "category")
    var category: String,

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "description")
    var description: String,

    @ColumnInfo(name = "location")
    var location: String,

    @ColumnInfo(name = "date")
    var date: String,

    @ColumnInfo(name = "time")
    var time: String,

    @ColumnInfo(name = "studentId")
    var studentId: Int,


)