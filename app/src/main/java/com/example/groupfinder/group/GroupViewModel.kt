package com.example.groupfinder.group

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.groupfinder.network.GroupFinderApi
import com.example.groupfinder.network.models.Group
import com.example.groupfinder.network.models.StudentCompact
import kotlinx.coroutines.*
import java.lang.Exception

/**
 * [AndroidViewModel] tilknyttet [GroupFragment], som inneholder informasjon om den valgte [Group].
 * Her hentes studentene som er medlem av gruppen (API kall/query)
 *
 * @author Anders Olai Pedersen - 225280
 */

class GroupViewModel(group: Group, app: Application) : AndroidViewModel(app) {

    // Valgt gruppe
    private val _selectedGroup = MutableLiveData<Group>()
    val selectedGroup: LiveData<Group>
        get() = _selectedGroup

    // Liste av gruppe medlemmer (Hentes av API kall)
    private val _groupMembers = MutableLiveData<List<StudentCompact>>()
    val groupMembers : LiveData<List<StudentCompact>>
        get() = _groupMembers

    // Brukes via et adapter for å vise knappen eller ikke.
    private val _displayButtons = MutableLiveData<Boolean>()
    val displayButtons: LiveData<Boolean>
        get() = _displayButtons

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

    init {
        _selectedGroup.value = group
        _displayButtons.value = false
        getGroupMembers(_selectedGroup.value!!.gId)
    }


    /**
     * @param groupId Henter gruppemedlemmene ved init
     */
    private fun getGroupMembers(groupId: Int) {
        coroutineScope.launch {
            val getGroupMembersDeferred = GroupFinderApi.retrofitService.getGroupMembersAsync(groupId)

            try {
                val listResult = getGroupMembersDeferred.await()

                _groupMembers.value = listResult.groupMembers
            } catch (e: Exception) {
                print("ERROR: ${e.message}")
                _groupMembers.value = ArrayList()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


    // Ubrukt metode
    fun displayGroupMembers(student: StudentCompact) {
        println(student)
    }
}