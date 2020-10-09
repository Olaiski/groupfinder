package com.example.groupfinder.userprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.groupfinder.network.GroupFinderApi

import com.example.groupfinder.network.models.Group
import com.example.groupfinder.network.models.Student
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception


enum class ApiStatus { LOADING, ERROR, DONE }
private const val NAME_LENGTH = 17

class UserProfileViewModel : ViewModel(){


    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
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

    private val _fullname = MutableLiveData<String>()
    val fullname: LiveData<String>
        get() = _fullname

    private val _phonenumber = MutableLiveData<String>()
    val phonenumber: LiveData<String>
        get() = _phonenumber

    private val _student = MutableLiveData<Student>()
    val student: LiveData<Student>
        get() = _student


    init {
        getStudent("th@msn.no")
        getGroups("th@msn.no")
    }



    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun displayGroupDetails(group: Group) {
        println(group)
        _navigateToSelectedGroup.value = group
    }

    fun displayGroupDetailsComplete() {
        _navigateToSelectedGroup.value = null
    }

    private fun concatName(s1: String, s2: String) : String? {
        println(s1.length + s2.length)
        return if (s1.length + s2.length >= NAME_LENGTH) s1 else "$s1 $s2"
    }


    private fun getGroups(email: String) {
        coroutineScope.launch {
            var getGroupsDeferred = GroupFinderApi.retrofitService.getUserGroupsAsync(email)

            try {
                _status.value = ApiStatus.LOADING
                val listResult = getGroupsDeferred.await()
                _status.value = ApiStatus.DONE
                _groups.value = listResult.userGroups

            } catch (e: Exception) {
                _status.value = ApiStatus.ERROR
                println("ERROR: $e")
                _groups.value = ArrayList()
            }
        }
    }

    private fun getStudent(email: String) {
        coroutineScope.launch {
            var getStudentDeferred = GroupFinderApi.retrofitService.getStudentAsync(email)

            try {
                loadStudentString()

                _status.value = ApiStatus.LOADING
                val result = getStudentDeferred.await()
                _status.value = ApiStatus.DONE

                _student.value = result.student
                _fullname.value = concatName(result.student.firstname, result.student.lastname)
                _phonenumber.value = result.student.phonenumber.toString()
                _email.value = result.student.email

            }catch (e : Exception) {
                _status.value = ApiStatus.ERROR
                println("ERROR: $e")
                loadStudentString()
            }

        }
    }

    private fun loadStudentString() {
        _fullname.value = "..."
        _phonenumber.value = "..."
        _email.value = "..."
    }



}

