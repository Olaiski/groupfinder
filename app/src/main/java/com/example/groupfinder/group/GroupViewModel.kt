package com.example.groupfinder.group

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.groupfinder.network.models.Group

class GroupViewModel(group: Group, app: Application) : AndroidViewModel(app) {

    private val _selectedGroup = MutableLiveData<Group>()
    val selectedGroup: LiveData<Group>
        get() = _selectedGroup

    init {
        _selectedGroup.value = group
    }


}