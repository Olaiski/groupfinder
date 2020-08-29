package com.example.groupfinder.userprofile

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.groupfinder.database.GroupFinderDatabaseDao
import java.lang.IllegalArgumentException


class UserProfileViewModelFactory(
    private val dataSource: GroupFinderDatabaseDao,
    private val application: Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserProfileViewModel::class.java)) {
            return UserProfileViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Uknown ViewModel class")
    }
}