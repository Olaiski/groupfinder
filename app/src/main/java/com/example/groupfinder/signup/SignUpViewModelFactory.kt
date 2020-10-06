package com.example.groupfinder.signup

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.groupfinder.database.GroupFinderDatabaseDao


/**
 * This is pretty much boiler plate code for a ViewModel Factory.
 *
 * Provides the GroupFinderDatabase and context to the ViewModel.
 */
class SignUpViewModelFactory(
    private val dataSource: GroupFinderDatabaseDao,
    private val application: Application) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
            return SignUpViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Uknown ViewModel class")
    }

}