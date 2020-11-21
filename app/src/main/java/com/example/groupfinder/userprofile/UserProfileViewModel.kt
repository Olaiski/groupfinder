package com.example.groupfinder.userprofile


import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.preference.PreferenceManager
import com.example.groupfinder.network.GroupFinderApi
import com.example.groupfinder.network.models.Group
import com.example.groupfinder.network.models.PostGroup
import com.example.groupfinder.network.models.Student
import kotlinx.coroutines.*


//private const val EMAIL = "dde@msn.no"
private lateinit var EMAIL: String

enum class ApiStatus { LOADING, ERROR, DONE }
private const val NAME_MAX_LENGTH = 17

class UserProfileViewModel : ViewModel(){

    /**
     * A [CoroutineScope] keeps track of all coroutines started by this ViewModel.
     *
     * Because we pass it [viewModelJob], any coroutine started in this uiScope can be cancelled
     * by calling `viewModelJob.cancel()`
     *
     * By default, all coroutines started in uiScope will launch in [Dispatchers.Main] which is
     * the main thread on Android. This is a sensible default because most coroutines started by
     * a [ViewModel] update the UI after performing some processing.
     */
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status


    private val _groups = MutableLiveData<List<Group>>()
    val groups: LiveData<List<Group>>
        get() = _groups

    // Internally, we use a MutableLiveData to handle navigation to the selected property
    private val _navigateToSelectedGroup = MutableLiveData<Group>()
    val navigateToSelectedGroup: LiveData<Group>
        get() = _navigateToSelectedGroup

    private val _loginSuccess = MutableLiveData<Boolean>()
    val loginSuccess: LiveData<Boolean>
        get() = _loginSuccess

    private val _createGroupSuccess = MutableLiveData<Boolean>()
    val createGroupSuccess: LiveData<Boolean>
        get() = _createGroupSuccess


    private val _sId = MutableLiveData<Int>()
    val sId: LiveData<Int>
        get() = _sId

    private val _email = MutableLiveData<String>()
    val email: LiveData<String>
        get() = _email

    private val _fullname = MutableLiveData<String>()
    val fullname: LiveData<String>
        get() = _fullname

    private val _phonenumber = MutableLiveData<String>()
    val phonenumber: LiveData<String>
        get() = _phonenumber

    private val _student = MutableLiveData<Student>()
    val student: LiveData<Student>
        get() = _student

    private var _groupName = MutableLiveData<String>()
    val groupName: LiveData<String>
        get() = _groupName

    private var _courseCode = MutableLiveData<String>()
    val courseCode: LiveData<String>
        get() = _courseCode

    private var _description = MutableLiveData<String>()
    val description: LiveData<String>
        get() = _description


    private var _showSnackBarEvent = MutableLiveData<Boolean>()
    val showSnackBarEvent: LiveData<Boolean>
        get() = _showSnackBarEvent

    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?>
        get() = _message



    init {
//        val email = _student.value?.email
        EMAIL = _email.value.toString()

    }


    fun doneShowingSnackbar() {
        _showSnackBarEvent.value = false
    }


    /**
     *  Navigates to group via click listener in [UserProfileFragment]
     */
    fun displayGroupDetails(group: Group) {
        _navigateToSelectedGroup.value = group
    }

    fun displayGroupDetailsComplete() {
        _navigateToSelectedGroup.value = null
    }


    /**
     * @param group POST request to backend with group information and studentId
     */
    fun onCreateGroup(group: PostGroup) {
        coroutineScope.launch {

            val postGroup = GroupFinderApi.retrofitService.postRegisterGroupAsync(group)

            try {
                val res = postGroup.await()
                val resMessage = res.message

//                getGroups(EMAIL)

                _createGroupSuccess.value = true

            }catch (e : Exception) {
                Log.i("PostGroup", e.toString())
            }
        }
    }


    /**
     * @param email gets groups related to student base on this email
     */
    fun getGroups(email: String) {
        coroutineScope.launch {
            val getGroupsDeferred = GroupFinderApi.retrofitService.getUserGroupsAsync(email)

            try {
                _status.value = ApiStatus.LOADING
                val listResult = getGroupsDeferred.await()
                _status.value = ApiStatus.DONE
                _groups.value = listResult.userGroups

            } catch (e: Exception) {
                _status.value = ApiStatus.ERROR
                println("ERROR: $e")
                _groups.value = ArrayList()
            }
        }
    }

    /**
     * @param email gets student information based on this email
     */
    fun getStudent(email: String) {
        coroutineScope.launch {
            val getStudentDeferred = GroupFinderApi.retrofitService.getStudentAsync(email)


            try {
                loadStudentString()

                _status.value = ApiStatus.LOADING
                val result = getStudentDeferred.await()
                _status.value = ApiStatus.DONE


                _student.value = result.student
                _fullname.value = concatName(result.student.firstname, result.student.lastname, NAME_MAX_LENGTH)
                _phonenumber.value = result.student.phonenumber.toString()
                _email.value = result.student.email
                _sId.value = result.student.id

            }catch (e : Exception) {
                _status.value = ApiStatus.ERROR
                println("ERROR: $e")
                loadStudentString()
            }

        }
    }


    fun onLogin(email: String, password: String){
        coroutineScope.launch {
            val postStudent = GroupFinderApi.retrofitService.postLoginStudentAsync(email, password)

            try {
                val res = postStudent.await()
                _message.value = res.message
                _student.value = res.student

                _email.value = res.student.email

                println(res)
                _loginSuccess.value = true
            }catch (e: java.lang.Exception) {
                Log.i("PostStudentLogin", e.toString())
                _message.value = "Incorrect login info"
            }

        }
    }


    private fun loadStudentString() {
        _fullname.value = "..."
        _phonenumber.value = "..."
        _email.value = "..."
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }



}

