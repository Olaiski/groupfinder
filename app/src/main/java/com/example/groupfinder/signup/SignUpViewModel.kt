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




    fun onCreateStudent(student: PostStudent) {
        coroutineScope.launch {
            val postStudent = GroupFinderApi.retrofitService.postRegisterStudentAsync(student)

            try {
                val res = postStudent.await()
                val resMessage = res.message

                println(resMessage)

            }catch (e: Exception) {
                Log.i("PostStudent", e.toString())
            }

        }
    }




    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}