package com.example.groupfinder.userprofile

import android.app.Application
import androidx.lifecycle.*
import com.example.groupfinder.database.GroupFinderDatabase
import com.example.groupfinder.database.GroupFinderDatabaseDao
import com.example.groupfinder.database.models.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class UserProfileViewModel(
    private val database: GroupFinderDatabaseDao,
    application: Application) : AndroidViewModel(application), ViewModelStoreOwner{


    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user


    override fun getViewModelStore(): ViewModelStore {
        TODO("Not yet implemented")
    }
}