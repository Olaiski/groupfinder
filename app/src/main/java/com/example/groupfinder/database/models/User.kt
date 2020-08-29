package com.example.groupfinder.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(

    @PrimaryKey(autoGenerate = true)
    var personId: Long = 0L,

    @ColumnInfo(name = "forename")
    var forename: String,

    @ColumnInfo(name = "lastname")
    var lastname: String,

    @ColumnInfo(name = "email")
    var email: String,

    @ColumnInfo(name = "phonenumber")
    var phonenumber: String,

    @ColumnInfo(name = "password")
    var password: String

)