package com.example.groupfinder.login.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.groupfinder.network.GroupFinderApi
import com.example.groupfinder.network.models.PostStudent
import com.example.groupfinder.network.models.Student
import kotlinx.coroutines.*
import java.lang.Exception

class LoginViewModel : ViewModel() {

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _student = MutableLiveData<Student>()
    val student: LiveData<Student>
        get() = _student

    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?>
        get() = _message





//    fun login(email: String, password: String) {
//        val result = loginRepository.login(email, password)
//    }



}