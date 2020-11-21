package com.example.groupfinder.group

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.groupfinder.network.models.Group

/**
 * Factory metode for [GroupViewModel]
 */
class GroupViewModelFactory(
    private val group: Group,
    private val application: Application) :ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GroupViewModel::class.java)) {
            return GroupViewModel(group, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}