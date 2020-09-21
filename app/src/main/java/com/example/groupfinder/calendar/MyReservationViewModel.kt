package com.example.groupfinder.calendar;

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.groupfinder.database.GroupFinderDatabaseDao
import com.example.groupfinder.formatReservations
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

// TODO: 21/09/2020 Endre dataSource
class MyReservationViewModel(val database: GroupFinderDatabaseDao,
                             application: Application ) : AndroidViewModel(application) {


    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)




//    val reservations = database.getMyReservations()

    private val _reservation = MutableLiveData<ArrayList<Reservation>>()
    val reservation: LiveData<ArrayList<Reservation>>
        get() = _reservation


    private val _navigateToReservation = MutableLiveData<Boolean>()
    val navigateToReservation: LiveData<Boolean>
        get() = _navigateToReservation


    fun doneNavigating() {
        _navigateToReservation.value = false
    }

    fun setNavigation(nav: Boolean) {
        _navigateToReservation.value = true
    }


    // TODO: 21/09/2020 Test data, skal bort.
    val resString = formatReservations(setDummyRes(), application.resources)
    fun setDummyRes() : ArrayList<Reservation> {
        val resDummyList = ArrayList<Reservation>()

        val res1 = Reservation("13:00", "15:00", "27/09/2020", "Bø", "GruppeNavn1", "4-113")
        val res2 = Reservation("16:00", "19:00", "29/09/2020", "Bø", "GruppeNavn1", "2-115")
        val res3 = Reservation("20:00", "23:00", "27/09/2020", "Bø", "GruppeNavn2", "4-115")

        resDummyList.add(res1)
        resDummyList.add(res2)
        resDummyList.add(res3)

        return resDummyList
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}

// TODO: 21/09/2020 Test data, skal bort.
class Reservation(
    val startTime: String,
    val endTime: String,
    val date: String,
    val location: String,
    val groupName: String,
    val roomNumber: String)