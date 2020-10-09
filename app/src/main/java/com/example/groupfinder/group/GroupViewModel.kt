package com.example.groupfinder.group

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.groupfinder.network.models.Group
import com.example.groupfinder.network.models.Student

class GroupViewModel(group: Group, app: Application) : AndroidViewModel(app) {

    private val _selectedGroup = MutableLiveData<Group>()
    val selectedGroup: LiveData<Group>
        get() = _selectedGroup

    private val _student = MutableLiveData<List<Student>>()
    val student : LiveData<List<Student>>
        get() = _student

    init {
        _selectedGroup.value = group
    }


    // TODO: 09/10/2020 Hent ut studenter for group fragment.. -> User_card_info -> BindingAdapter? 
    private fun getGroupMembers() {
        
    }



}