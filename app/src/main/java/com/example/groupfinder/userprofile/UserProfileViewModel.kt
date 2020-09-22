package com.example.groupfinder.userprofile

import android.app.Application
import androidx.lifecycle.*
import com.example.groupfinder.calendar.Reservation
import com.example.groupfinder.database.GroupFinderDatabase
import com.example.groupfinder.database.GroupFinderDatabaseDao
import com.example.groupfinder.database.models.User
import com.example.groupfinder.formatGroup
import com.example.groupfinder.formatReservations
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

// TODO: 21/09/2020 Endre dataSource
class UserProfileViewModel(
    private val database: GroupFinderDatabaseDao,
    application: Application) : AndroidViewModel(application){


    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    // TODO: 22/09/2020 Erstattes med LiveData og Queries
    val resString = formatGroup(setDummyRes(), application.resources)
    fun setDummyRes() : ArrayList<Group> {
        val resDummyList = ArrayList<Group>()

        val g1 = Group("Mob3000", "Prosjekt", "Jobber med.. bla bla")
        val g2 = Group("Alg. og datastrukturer", "Studiegruppe", "Jobber med sort algorithms")
        val g3 = Group("Spesialpensum", "Studiegruppe", "Jobber med.. ")

        resDummyList.add(g1)
        resDummyList.add(g2)
        resDummyList.add(g3)

        return resDummyList
    }







    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}

// TODO: 22/09/2020 Dummy data, skal bort
class Group(
    val courseCode: String,
    val title: String,
    val description: String)