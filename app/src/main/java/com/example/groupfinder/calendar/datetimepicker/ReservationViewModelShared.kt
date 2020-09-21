package com.example.groupfinder.calendar.datetimepicker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

// TODO: 20/09/2020 Denne klassen må opprette connection til database, hente ut data/queries etc..
class ReservationViewModelShared : ViewModel() {



    private lateinit var timeStringList: ArrayList<String>
    private lateinit var roomStringList: ArrayList<String>
    private lateinit var groupStringList: ArrayList<String>


    companion object {

        const val START_TIME = 8
        const val END_TIME = 23
        const val MAX_HOUR_RESERVATION = 4

    }


    private val _startTime = MutableLiveData<String>()
    val startTime: LiveData<String>
        get() = _startTime


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



    private val _endTime = MutableLiveData<String>()
    val endTime: LiveData<String>
        get() = _endTime

    fun endTimeSelected(item: String) {
        _endTime.value = item
    }



    // Ex: Query fra DB..
    // val rooms = database.getAllRooms()
    // Room number
    private val _roomNumber = MutableLiveData<String>()
    val roomNumber: LiveData<String>
        get() = _roomNumber

    fun setRoomNumberList() : ArrayList<String> {
        roomStringList = ArrayList()
        for (i in 110..145) {
            roomStringList.add("4 -$i")
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

    fun setGroupList() : ArrayList<String> {
        groupStringList = ArrayList()

        groupStringList.add("G1")
        groupStringList.add("G2")
        groupStringList.add("G3")

        return groupStringList
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
        println("$startTime $endTime $roomNumber $groupName")
        _showSnackBarEvent.value = true
    }

}


