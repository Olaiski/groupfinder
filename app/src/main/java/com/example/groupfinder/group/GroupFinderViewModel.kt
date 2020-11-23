package com.example.groupfinder.group

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.groupfinder.network.GroupFinderApi
import com.example.groupfinder.network.models.Group
import com.example.groupfinder.userprofile.ApiStatus
import com.example.groupfinder.userprofile.UserProfileFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * [GroupFinderViewModel] er der man søker etter gruppe, her kommer en liste med grupper fra API kall.
 *
 * @author Anders Olai Pedersen - 225280
 */
class GroupFinderViewModel : ViewModel() {

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



    private val _groups = MutableLiveData<List<Group>>()
    val groups: LiveData<List<Group>>
        get() = _groups

    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status

    // Navigering
    private val _navigateToSelectedGroup = MutableLiveData<Group>()
    val navigateToSelectedGroup: LiveData<Group>
        get() = _navigateToSelectedGroup



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
     *  Henter alle gruppene, setter status / bilde.
     */
    fun getGroups() {
        coroutineScope.launch {
            val getGroupsDeferred = GroupFinderApi.retrofitService.getAllGroupsAsync()

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

}