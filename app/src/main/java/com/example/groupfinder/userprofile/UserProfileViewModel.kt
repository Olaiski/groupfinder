package com.example.groupfinder.userprofile

import android.app.Application
import androidx.lifecycle.*
import com.example.groupfinder.database.GroupFinderDatabase
import com.example.groupfinder.database.GroupFinderDatabaseDao
import com.example.groupfinder.database.models.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

// TODO: 21/09/2020 Endre dataSource
class UserProfileViewModel(
    private val database: GroupFinderDatabaseDao,
    application: Application) : AndroidViewModel(application){


    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)












    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}