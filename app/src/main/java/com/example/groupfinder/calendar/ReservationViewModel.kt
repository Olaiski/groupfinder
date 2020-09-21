package com.example.groupfinder.calendar

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.groupfinder.database.GroupFinderDatabaseDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job


// TODO: 21/09/2020 Endre dataSource
class ReservationViewModel(val dataSource: GroupFinderDatabaseDao,
                           application: Application) : AndroidViewModel(application) {



    /**
     * viewModelJob allows us to cancel all coroutines started by this ViewModel.
     */
    private val viewModelJob = Job()


    /**
     * A [CoroutineScope] keeps track of all coroutines started by this ViewModel.
     *
     * Because we pass it [viewModelJob], any coroutine started in this uiScope can be cancelled
     * by calling `viewModelJob.cancel()`
     *
     * By default, all coroutines started in uiScope will launch in [Dispatchers.Main] which is
     * the main thread on Android. This is a sensible default because most coroutines started by
     * a [ViewModel] update the UI after performing some processing.
     */
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)





    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}