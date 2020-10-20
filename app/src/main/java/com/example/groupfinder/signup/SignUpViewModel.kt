package com.example.groupfinder.signup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.groupfinder.database.models.User
import com.example.groupfinder.network.GroupFinderApi
import com.example.groupfinder.network.models.PostStudent
import kotlinx.coroutines.*
import java.lang.Exception

class SignUpViewModel : ViewModel() {

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    private val _student = MutableLiveData<PostStudent>()
    val student: LiveData<PostStudent>
        get() = _student


    private val _navigateToLogin = MutableLiveData<Boolean>()
    val navigateToLogin: LiveData<Boolean>
        get() = _navigateToLogin

    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?>
        get() = _message

    private val _email = MutableLiveData<String>()
    val email: LiveData<String>
        get() = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String>
        get() = _password


    init {

    }


    fun onCreateStudent(fname: String, lname: String, email: String, phone: Int, password: String) {
        coroutineScope.launch {
            val postStudent = GroupFinderApi.retrofitService.postRegisterStudentAsync(fname,lname,email,phone,password)

            try {
                val res = postStudent.await()
                _message.value = res.message

                _navigateToLogin.value = true
            }catch (e: Exception) {
                Log.i("PostStudent", e.toString())
                _message.value = "Email already in database"
            }

        }
    }

    fun navigateToLoginComplete() {
        _navigateToLogin.value = false
    }




    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}