package com.example.groupfinder.login.ui.login

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.groupfinder.database.GroupFinderDatabaseDao
import com.example.groupfinder.login.data.LoginDataSource
import com.example.groupfinder.login.data.LoginRepository


/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
class LoginViewModelFactory(
    private val dataSource: GroupFinderDatabaseDao,
    private val application: Application) : ViewModelProvider.Factory {


    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(
                loginRepository = LoginRepository(
                    dataSource = LoginDataSource()
                ), application
            ) as T
        }
        throw IllegalArgumentException("Uknown ViewModel class")
    }

}