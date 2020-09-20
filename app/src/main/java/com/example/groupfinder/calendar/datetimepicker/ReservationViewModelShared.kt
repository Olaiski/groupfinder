package com.example.groupfinder.calendar.datetimepicker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

// TODO: 20/09/2020 Denne klassen må opprette connection til database, hente ut data/queries etc..
class ReservationViewModelShared : ViewModel() {



    private val _startTime = MutableLiveData<String>()
    val startTime: LiveData<String>
        get() = _startTime

    private val _endTime = MutableLiveData<String>()
    val endTime: LiveData<String>
        get() = _endTime



    private lateinit var timeStringList: ArrayList<String>
    private lateinit var roomStringList: ArrayList<String>


    companion object {

        const val START_TIME = 8
        const val END_TIME = 23
        const val MAX_HOUR_RESERVATION = 4

    }



    // Ex:
    // val rooms = database.getAllRooms()


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


    fun endTimeSelected(item: String) {
        _endTime.value = item
    }


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


}


