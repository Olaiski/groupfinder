package com.example.groupfinder.signup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.groupfinder.network.GroupFinderApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * [SignUpViewModel] viewModel for registrering.
 *
 * @author Anders Olai Pedersen - 225280
 */
class SignUpViewModel : ViewModel() {

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    private val _navigateToLogin = MutableLiveData<Boolean>()
    val navigateToLogin: LiveData<Boolean>
        get() = _navigateToLogin


    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?>
        get() = _message



    /**
     * Oppretter ny student via [GroupFinderApi] kall.
     * @params Student info
     */
    fun onCreateStudent(fname: String, lname: String, email: String, phone: Int, password: String) {
        coroutineScope.launch {
            val postStudent = GroupFinderApi.retrofitService.postRegisterStudentAsync(fname,lname,email,phone,password)

            try {
                val res = postStudent.await()
                _message.value = res.message

                _navigateToLogin.value = true
            }catch (e: Exception) {
                Log.i("PostStudent", e.toString())

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