package com.example.groupfinder.group

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.groupfinder.network.GroupFinderApi
import com.example.groupfinder.network.models.Group
import com.example.groupfinder.network.models.StudentCompact
import kotlinx.coroutines.*
import java.lang.Exception

/**
 * The [ViewModel] associated with the [GroupFragment], containing information about the selected [Group]
 */

class GroupViewModel(group: Group, app: Application) : AndroidViewModel(app) {

    private val _selectedGroup = MutableLiveData<Group>()
    val selectedGroup: LiveData<Group>
        get() = _selectedGroup

    private val _groupMembers = MutableLiveData<List<StudentCompact>>()
    val groupMembers : LiveData<List<StudentCompact>>
        get() = _groupMembers

    private val _displayButtons = MutableLiveData<Boolean>()
    val displayButtons: LiveData<Boolean>
        get() = _displayButtons


    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    init {
        _selectedGroup.value = group
        _displayButtons.value = false
        getGroupMembers(_selectedGroup.value!!.gId)
    }


    // TODO: 09/10/2020 Hent ut studenter for group fragment.. -> User_card_info -> BindingAdapter? 
    private fun getGroupMembers(groupId: Int) {
        coroutineScope.launch {
            val getGroupMembersDeferred = GroupFinderApi.retrofitService.getGroupMembersAsync(groupId)

            println(groupId)

            try {
                val listResult = getGroupMembersDeferred.await()

                _groupMembers.value = listResult.groupMembers
            } catch (e: Exception) {
                print("ERROR: ${e.message}")
                _groupMembers.value = ArrayList()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


    fun displayGroupMembers(student: StudentCompact) {
        println(student)
    }



}