package com.example.groupfinder.userprofile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.groupfinder.database.GroupFinderDatabaseDao
import com.example.groupfinder.network.GroupFinderApi
import com.example.groupfinder.network.models.Group
import com.example.groupfinder.network.models.PostGroup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch



class CreateGroupViewModel : ViewModel() {

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


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
    fun onCreateGroup(group: PostGroup) {
      uiScope.launch {

          try {
              var postGroupsDeferred = GroupFinderApi.retrofitService.postRegisterGroup(group)


          } catch (e : Exception) {
              println("ERROR: $e")
          }


      }
        _showSnackBarEvent.value = true
    }




    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}