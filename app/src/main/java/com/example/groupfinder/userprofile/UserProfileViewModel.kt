package com.example.groupfinder.userprofile


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.groupfinder.network.GroupFinderApi
import com.example.groupfinder.network.models.Group
import com.example.groupfinder.network.models.PostGroup
import com.example.groupfinder.network.models.Student
import com.example.groupfinder.util.PreferenceProvider
import com.example.groupfinder.util.concatName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


enum class ApiStatus { LOADING, ERROR, DONE }
private const val NAME_MAX_LENGTH = 17
/**
 *  [UserProfileViewModel] Inneholder informasjon om brukeren og gruppene han/hun er meldem av.
 *  Blir delt mellom [UserProfileFragment] og [CreateGroupDialogFragment] siden dialogen er en del av fragmentet.
 *
 *  @author Anders Olai Pedersen - 225280
 */
class UserProfileViewModel : ViewModel(){

    /**
     * A [CoroutineScope] holder oversikt over alle coroutines startet av denne ViewModel.
     *
     * Fordi vi passerer en [viewModelJob], kan enhver coroutine som startes i dette uiScope(et) bli kansellert
     * ved å kalle `viewModelJob.cancel ()`
     *
     * Som standard vil alle coroutines startet i uiScope starte i [Dispatchers.Main] som er
     * hovedtråden på Android. Dette er en fornuftig standard fordi de fleste coroutines startet av
     * a [ViewModel] oppdaterer brukergrensesnittet etter endt behandling.
     */
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    /**
     * API status, setter bilde basert på [ApiStatus]
     */
    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status


    // Gruppene til brukeren
    private val _groups = MutableLiveData<List<Group>>()
    val groups: LiveData<List<Group>>
        get() = _groups

    // Navigering
    private val _navigateToSelectedGroup = MutableLiveData<Group>()
    val navigateToSelectedGroup: LiveData<Group>
        get() = _navigateToSelectedGroup

    // Login og opprettelse av gruppe boolean
    private val _loginSuccess = MutableLiveData<Boolean>()
    val loginSuccess: LiveData<Boolean>
        get() = _loginSuccess

    private val _createGroupSuccess = MutableLiveData<Boolean>()
    val createGroupSuccess: LiveData<Boolean>
        get() = _createGroupSuccess

    // Student id
    private val _sId = MutableLiveData<Int>()
    val sId: LiveData<Int>
        get() = _sId

    // Epost
    private val _email = MutableLiveData<String>()
    val email: LiveData<String>
        get() = _email

    // Navn
    private val _fullname = MutableLiveData<String>()
    val fullname: LiveData<String>
        get() = _fullname

    // Telefon
    private val _phonenumber = MutableLiveData<String>()
    val phonenumber: LiveData<String>
        get() = _phonenumber

    // Student objekt
    private val _student = MutableLiveData<Student>()
    val student: LiveData<Student>
        get() = _student


    private var _showSnackBarEvent = MutableLiveData<Boolean>()
    val showSnackBarEvent: LiveData<Boolean>
        get() = _showSnackBarEvent

    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?>
        get() = _message



    fun doneShowingSnackbar() {
        _showSnackBarEvent.value = false
    }

    /**
     *  Navigerer til [Group] via clicklistener i [UserProfileFragment]
     */
    fun displayGroupDetails(group: Group) {
        _navigateToSelectedGroup.value = group
    }

    fun displayGroupDetailsComplete() {
        _navigateToSelectedGroup.value = null
    }


    /**
     * Oppretter gruppe
     * @param group
     * Request til backend med [PostGroup] informasjon
     */
    fun onCreateGroup(group: PostGroup) {
        coroutineScope.launch {

            val postGroup = GroupFinderApi.retrofitService.postRegisterGroupAsync(group)

            try {
                val res = postGroup.await()
                val resMessage = res.message


                _createGroupSuccess.value = true
            }catch (e : Exception) {
                Log.i("PostGroup", e.toString())
            }
        }
    }


    /**
     *
     * @param email Henter alle gruppene relatert til studenten basert på epost
     */
    fun getGroups(email: String) {
        coroutineScope.launch {
            val getGroupsDeferred = GroupFinderApi.retrofitService.getUserGroupsAsync(email)

            try {
                _status.value = ApiStatus.LOADING
                val listResult = getGroupsDeferred.await()
                println(listResult)
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
     *
     * @param email Henter student informasjon basert på epost
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


    /**
     * Login funksjon
     * @params Epost og passord sendes til backend
     */
    fun onLogin(email: String, password: String){
        coroutineScope.launch {
            val postStudent = GroupFinderApi.retrofitService.postLoginStudentAsync(email, password)

            try {
                val res = postStudent.await()
                _message.value = res.message
                _student.value = res.student

                _email.value = res.student.email
                _sId.value = res.student.id

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

