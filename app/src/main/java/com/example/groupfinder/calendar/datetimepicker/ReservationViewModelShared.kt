package com.example.groupfinder.calendar.datetimepicker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.groupfinder.network.GroupFinderApi
import com.example.groupfinder.network.models.GroupLeaderGroup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

// TODO: 20/09/2020 Denne klassen må opprette connection til database, hente ut data/queries etc..
class ReservationViewModelShared : ViewModel() {


    // TODO: 22/09/2020 Dummy data
    private lateinit var timeStringList: ArrayList<String>
    private lateinit var roomStringList: ArrayList<String>
    private lateinit var groupStringList: ArrayList<String>


    companion object {

        const val START_TIME = 8
        const val END_TIME = 23
        const val MAX_HOUR_RESERVATION = 4

    }


    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _email = MutableLiveData<String>()
    val email: LiveData<String>
        get() = _email

    private val _startTime = MutableLiveData<String>()
    val startTime: LiveData<String>
        get() = _startTime

    private val _groups = MutableLiveData<List<GroupLeaderGroup>>()
    val groups: LiveData<List<GroupLeaderGroup>>
        get() = _groups


    private val _endTime = MutableLiveData<String>()

    val endTime: LiveData<String>
        get() = _endTime

    fun endTimeSelected(item: String) {
        _endTime.value = item
    }

    fun setTimeList() : ArrayList<String> {
        timeStringList = ArrayList()
        for (i in START_TIME..END_TIME) {
            timeStringList.add("$i:00")
        }
        return timeStringList
    }

    fun startTimeSelected(item: String) {
        _startTime.value = item
    }




    // Ex: Query fra DB..
    // val rooms = database.getAllRooms()
    // Room number
    private val _roomNumber = MutableLiveData<String>()
    val roomNumber: LiveData<String>
        get() = _roomNumber


    // TODO: 16/11/2020 Query til db basert på tid
    fun setRoomNumberList() : ArrayList<String> {
        roomStringList = ArrayList()
        for (i in 110..145) {
            roomStringList.add("4-$i")
        }
        return roomStringList
    }

    fun roomNumberSelected(item: String) {
        _roomNumber.value = item
    }

    // Ex: Query fra DB..
    // val groups = database.getUserGroups()
    // Group
    private val _groupName = MutableLiveData<String>()
    val groupName: LiveData<String>
        get() = _groupName


    // TODO: 16/11/2020 Query til db, henter innlogged bruker sine grupper 
    fun getGroups(email: String){
        coroutineScope.launch {
            val getLeaderGroupsDeferred = GroupFinderApi.retrofitService.getGroupLeaderGroups(email)

            try {
                val listResult = getLeaderGroupsDeferred.await()
                println("Listresult group leader $listResult")

                _groups.value = listResult.groupLeaderGroups
            } catch (e: Exception) {
                println(e.message)
                _groups.value = ArrayList()
            }
        }
    }

    fun printGroup(group: GroupLeaderGroup) {
        println(group)
    }

    fun groupSelected(item: String) {
        _groupName.value = item
    }


    private val _showSnackBarEvent = MutableLiveData<Boolean>()
    val showSnackBarEvent: LiveData<Boolean>
        get() = _showSnackBarEvent

    fun doneShowingSnackbar() {
        _showSnackBarEvent.value = false
    }


    // TODO: 21/09/2020 DB Query..
    fun onReserveRoom(startTime: String, endTime: String, date: String, roomNumber: String, groupName: String) {
        println("$startTime $endTime $date $roomNumber $groupName")

        _showSnackBarEvent.value = true
//        _navigateToMyReservations.value = true
    }





    // Navigation - After reservation btn clicked
    private val _navigateToMyReservations = MutableLiveData<Boolean>()
    val navigateToMyReservations: LiveData<Boolean>
        get() = _navigateToMyReservations


    fun onReservationComplete() {
        _navigateToMyReservations.value = false
    }


}


