package com.example.groupfinder.calendar.datetimepicker

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.groupfinder.network.GroupFinderApi
import com.example.groupfinder.network.models.GroupLeaderGroup
import com.example.groupfinder.network.models.Reservation
import com.example.groupfinder.network.models.Room
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

    private val _vacantRoomList = MutableLiveData<List<Room>>()
    val vacantRoomList: LiveData<List<Room>>
        get() = _vacantRoomList




    private val _selectedDate = MutableLiveData<String>()
    val selectedDate: LiveData<String>
        get() = _selectedDate




    private val _groups = MutableLiveData<List<GroupLeaderGroup>>()
    val groups: LiveData<List<GroupLeaderGroup>>
        get() = _groups


    // Group
    private val _groupName = MutableLiveData<String>()
    val groupName: LiveData<String>
        get() = _groupName

    private val _groupId = MutableLiveData<Int>()
    val groupId: LiveData<Int>
        get() = _groupId


    // Rom
    private val _roomName = MutableLiveData<String>()
    val roomName: LiveData<String>
        get() = _roomName

    private val _roomId = MutableLiveData<Int>()
    val roomId: LiveData<Int>
        get() = _roomId



    // Navigation - After reservation btn clicked
    private val _navigateToMyReservations = MutableLiveData<Boolean>()
    val navigateToMyReservations: LiveData<Boolean>
        get() = _navigateToMyReservations


    private val _showSnackBarEvent = MutableLiveData<Boolean>()
    val showSnackBarEvent: LiveData<Boolean>
        get() = _showSnackBarEvent



    fun startTimeSelected(item: String) {
        println("StartTime = $item")
        _startTime.value = item
    }

    fun endTimeSelected(item: String) {
        println("EndTime = $item")
        _endTime.value = item

        getVacantRooms(_selectedDate.value.toString(), _startTime.value.toString(), _endTime.value.toString())
    }

    fun roomNumberSelected(item: Room) {
        _roomName.value = item.name
        _roomId.value = item.id
    }

    fun groupSelected(item: String) {
        println("Group = $item")
        _groupName.value = item
    }

    fun selectedGroupId(group: GroupLeaderGroup) {
        _groupName.value = group.groupName
        _groupId.value = group.id
    }

    fun doneShowingSnackbar() {
        _showSnackBarEvent.value = false
    }

    fun clearData() {
        _startTime.value = ""
        _endTime.value = ""
        _selectedDate.value = ""
        _roomName.value = ""
        _roomId.value = 0
        _groupId.value = 0
        _groupName.value = ""
        _vacantRoomList.value = ArrayList()
        _startTimeList.value = ArrayList()
        _endTimeList.value = ArrayList()
    }




    private fun getVacantRooms(date: String, start: String, end: String) {
        coroutineScope.launch {
            // "2020-11-16", "12:00", "16:00"
            val getVacantRoomsDeferred = GroupFinderApi.retrofitService.getVacantRoomsAsync(date = date, start = start, end = end)

            try {
                val listResult = getVacantRoomsDeferred.await()

                val msg = listResult.message
                println(msg)


                _vacantRoomList.value = listResult.vacantRooms

            } catch (e: Exception) {
                e.printStackTrace()
                println(e.message)
            }
        }
    }


    // Query til db, henter innlogged bruker sine grupper (der han/hun er leder)
    fun getGroups(email: String){
        coroutineScope.launch {
            val getLeaderGroupsDeferred = GroupFinderApi.retrofitService.getGroupLeaderGroupsAsync(email)

            try {
                val listResult = getLeaderGroupsDeferred.await()
                val msg = listResult.message

                println(msg)

                _groups.value = listResult.groupLeaderGroups
            } catch (e: Exception) {
                println(e.message)
                _groups.value = ArrayList()
            }
        }
    }



    fun onReserveRoom() {

        val startDate = "${_selectedDate.value} ${_startTime.value}"
        val endDate = "${_selectedDate.value} ${_endTime.value}"
        val roomId = _roomId.value
        val groupId = _groupId.value


        coroutineScope.launch {
            val reservation = Reservation(startDate, endDate, roomId, groupId)

            val postReservation = GroupFinderApi.retrofitService.postReserveRoomAsync(reservation)



            _navigateToMyReservations.value = true

            try {
                val res = postReservation.await()
                val resMessage = res.message

                println(resMessage)


            }catch (e: Exception) {
                print("ERROR: ${e.message}")
                e.printStackTrace()
            }

        }
        _showSnackBarEvent.value = true
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


