package com.example.groupfinder.userprofile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.groupfinder.database.GroupFinderDatabaseDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


// TODO: 21/09/2020 Endre dataSource
class CreateGroupViewModel(
    private val database: GroupFinderDatabaseDao,
    application: Application) : AndroidViewModel(application){

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    // TODO: 21/09/2020 Opprette gruppe, må lage queries til database
    private var _groupName = MutableLiveData<String>()
    val groupName: LiveData<String>
        get() = _groupName

    private var _courseCode = MutableLiveData<String>()
    val courseCode: LiveData<String>
        get() = _courseCode

    private var _description = MutableLiveData<String>()
    val description: LiveData<String>
        get() = _description


    private var _showSnackBarEvent = MutableLiveData<Boolean>()
    val showSnackBarEvent: LiveData<Boolean>
        get() = _showSnackBarEvent

    fun doneShowingSnackbar() {
        _showSnackBarEvent.value = false
    }


    // TODO: 22/09/2020 DB Query 
    fun onCreateGroup(groupName: String, courseCode: String, desc: String) {
      uiScope.launch {
          println("$groupName $courseCode $desc")
      }
        _showSnackBarEvent.value = true
    }




    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}