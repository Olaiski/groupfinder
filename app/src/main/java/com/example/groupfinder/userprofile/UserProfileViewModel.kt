package com.example.groupfinder.userprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.groupfinder.network.models.Group
import com.example.groupfinder.network.models.ResponseStudent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

enum class GroupApiStatus { LOADING, ERROR, DONE }
private const val NAME_LENGTH = 20


class UserProfileViewModel : ViewModel(){


    private val _status = MutableLiveData<GroupApiStatus>()
    val status: LiveData<GroupApiStatus>
        get() = _status


    private val _groups = MutableLiveData<List<Group>>()
    val groups: LiveData<List<Group>>
        get() = _groups

    // Internally, we use a MutableLiveData to handle navigation to the selected property
    private val _navigateToSelectedGroup = MutableLiveData<Group>()
    val navigateToSelectedGroup: LiveData<Group>
        get() = _navigateToSelectedGroup

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()
    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    private val _email = MutableLiveData<String>()
    val email: LiveData<String>
        get() = _email

    private val _firstname = MutableLiveData<String>()
    val firstname: LiveData<String>
        get() = _firstname

    private val _lastname = MutableLiveData<String>()
    val lastname: LiveData<String>
        get() = _lastname

    private val _phonenumber = MutableLiveData<String>()
    val phonenumber: LiveData<String>
        get() = _phonenumber



    init {
        _email.value = "dde@msn.no"
    }

    fun setGroups(groups: List<Group>) {
        _groups.value = groups
    }

    fun setStudent(resStudent: ResponseStudent) {
        _firstname.value = resStudent.student.firstname
        _lastname.value = resStudent.student.lastname
        _email.value = resStudent.student.email
        _phonenumber.value = resStudent.student.phonenumber.toString()

        _firstname.value = concatName(resStudent.student.firstname, resStudent.student.lastname)
    }


//    private fun getUserGroups(email: String) {
//
//    }






//    private fun getUserGroups(email: String) {
//        coroutineScope.launch {
//            var getGroupsDeferred = GroupFinderApi.retrofitService.getUserGroups(email)
//
//            try {
//                _status.value = GroupApiStatus.LOADING
//                val listResult = getGroupsDeferred.await()
//                _status.value = GroupApiStatus.DONE
//                _groups.value = listResult
//
//                println("OK!")
//                println(listResult)
//
//            } catch (e: Exception) {
//                _status.value = GroupApiStatus.ERROR
//                _groups.value = ArrayList()
//                println("ERRROR")
//            }
//
//        }
//    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

//    fun displayGroupDetails(group: Group) {
//        _navigateToSelectedGroup.value = group
//    }

    fun displayGroupDetails(group: GroupListAdapter.GroupListEvent) {
        println("Hey")
    }

    fun displayGroupDetailsComplete() {
        _navigateToSelectedGroup.value = null
    }

    private fun concatName(s1: String, s2: String) : String? {
        return if (s1.length + s2.length > NAME_LENGTH) s1 else "$s1 $s2"
    }





}

