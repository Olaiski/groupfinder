package com.example.groupfinder.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.groupfinder.database.models.User
import com.example.groupfinder.login.data.model.LoggedInUser
import java.io.IOException
import java.util.*


@Dao
interface GroupFinderDatabaseDao {


    @Insert
    fun insert(user: User)

    @Update
    fun update(user: User)

    @Query("SELECT * FROM user_table WHERE personId = :key")
    fun get(key: Long): User?

    @Query("SELECT * FROM user_table WHERE email = :key")
    fun getUserByEmail(key: String): User?




}