package com.example.groupfinder.calendar;

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.groupfinder.database.GroupFinderDatabaseDao
import java.lang.IllegalArgumentException

// TODO: 21/09/2020 Endre dataSource
public class MyReservationViewModelFactory(
                private val dataSource: GroupFinderDatabaseDao,
                private val application: Application) : ViewModelProvider.Factory {


    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyReservationViewModel::class.java)) {
            return MyReservationViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
