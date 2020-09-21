package com.example.groupfinder.userprofile

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.groupfinder.database.GroupFinderDatabaseDao
import java.lang.IllegalArgumentException

class CreateGroupViewModelFactory(
    private val dataSource: GroupFinderDatabaseDao,
    private val application: Application) : ViewModelProvider.Factory {


    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateGroupViewModel::class.java)) {
            return CreateGroupViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}