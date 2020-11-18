package com.example.groupfinder.calendar.datetimepicker

import android.os.Build
import androidx.annotation.RequiresApi
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
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.*
import kotlin.collections.ArrayList

// TODO: 20/09/2020 Denne klassen må opprette connection til database, hente ut data/queries etc..
class ReservationViewModelShared : ViewModel() {


    // TODO: 22/09/2020 Dummy data
    private lateinit var timeStringList: ArrayList<String>
    private lateinit var roomStringList: ArrayList<String>


    companion object {
        const val START_TIME = 8
        const val END_TIME = 23
        const val MAX_HOUR_RESERVATION = 4
        const val LAST_NUM_WITH_PREFIX_ZERO = 9
    }


    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _email = MutableLiveData<String>()
    val email: LiveData<String>
        get() = _email

    private val _startTime = MutableLiveData<String>()
    val startTime: LiveData<String>
        get() = _startTime

    private val _startTimeList = MutableLiveData<List<String>>()
    val startTimeList: LiveData<List<String>>
        get() = _startTimeList


    private val _endTime = MutableLiveData<String>()
    val endTime: LiveData<String>
        get() = _endTime

    private val _endTimeList = MutableLiveData<List<String>>()
    val endTimeList: LiveData<List<String>>
        get() = _endTimeList


    private val _selectedDate = MutableLiveData<String>()
    val selectedDate: LiveData<String>
        get() = _selectedDate


    private val _groups = MutableLiveData<List<GroupLeaderGroup>>()
    val groups: LiveData<List<GroupLeaderGroup>>
        get() = _groups

    // Ex: Query fra DB..
    // val groups = database.getUserGroups()
    // Group
    private val _groupName = MutableLiveData<String>()
    val groupName: LiveData<String>
        get() = _groupName

    // Ex: Query fra DB..
    // val rooms = database.getAllRooms()
    // Room number
    private val _roomNumber = MutableLiveData<String>()
    val roomNumber: LiveData<String>
        get() = _roomNumber



    // Navigation - After reservation btn clicked
    private val _navigateToMyReservations = MutableLiveData<Boolean>()
    val navigateToMyReservations: LiveData<Boolean>
        get() = _navigateToMyReservations


    fun endTimeSelected(item: String) {
        _endTime.value = item
    }





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


    // Query til db, henter innlogged bruker sine grupper (der han/hun er leder)
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
        _groupName.value = group.groupName
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


    fun getStartTimeList() : ArrayList<String> {
        return _startTimeList.value as ArrayList<String>
    }

    fun getEndTimeList() : ArrayList<String> {
        val startTime: Int? = _startTime.value?.substring(0,2)?.toInt()
        val endTime: Int? = startTime?.plus(MAX_HOUR_RESERVATION)

        _endTimeList.value = ArrayList()
        if (startTime != null) {
            for (i in startTime+1..endTime!!) {
                if (i <= LAST_NUM_WITH_PREFIX_ZERO)
                    (_endTimeList.value as ArrayList<String>).add("0$i:00")
                else
                    (_endTimeList.value as ArrayList<String>).add("$i:00")

            }

        }

        return _endTimeList.value as ArrayList<String>
    }

    fun startTimeSelected(item: String) {
        println(item)
        _startTime.value = item
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setDateStartTime(date: Long){
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)

        val dateInput = Date(date)
        val dateInputFormat = format.format(dateInput)

        val currentDate = Date()
        val currentDateFormat = format.format(currentDate)

        _selectedDate.value = dateInputFormat

        if (currentDateFormat != dateInputFormat){
            _startTimeList.value = ArrayList()

            for (i in START_TIME..END_TIME - MAX_HOUR_RESERVATION) {
                if (i <= LAST_NUM_WITH_PREFIX_ZERO)
                    (_startTimeList.value as ArrayList<String>).add("0$i:00")
                else
                    (_startTimeList.value as ArrayList<String>).add("$i:00")
            }

        }
        else {
            val nearestHour = getNearestHour(LocalTime.now())
            _startTimeList.value = ArrayList()

            for (i in nearestHour..END_TIME - MAX_HOUR_RESERVATION) {
                if (i <= LAST_NUM_WITH_PREFIX_ZERO)
                    (_startTimeList.value as ArrayList<String>).add("0$i:00")
                else
                    (_startTimeList.value as ArrayList<String>).add("$i:00")
            }
        }


    }



    @RequiresApi(Build.VERSION_CODES.O)
    private fun getNearestHour(now: LocalTime): Int {
        var hourReturn = 0

        var hour = now.hour
        val min = now.minute

        if (min < 30) {
            hourReturn = hour
        }
        else if (min >= 30) {
            ++hour
            hourReturn = hour
        }

        return hourReturn
    }

    fun onReservationComplete() {
        _navigateToMyReservations.value = false
    }


}


