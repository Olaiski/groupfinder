package com.example.groupfinder.calendar

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.groupfinder.network.GroupFinderApi
import com.example.groupfinder.network.models.*
import com.example.groupfinder.userprofile.ApiStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.*
import kotlin.collections.ArrayList

/**
 * [ReservationViewModelShared] er en delt [ViewModel], den blir brukt for å samle data om reservasjon.
 * Her blir queries mot API/Databasen sendt / mottatt, og dataene er lagret som LiveData (Når du oppdaterer verdien som er lagret i LiveData-objektet,
 * utløser den alle registrerte observatører så lenge den vedlagte LifecycleOwner er i aktiv tilstand.)
 *
 *
 * @author Anders Olai Pedersen - 225280
 */

class ReservationViewModelShared : ViewModel() {


    /**
     * Konstanter for reservasjon, her kan man enkelt endre tidspunktene.
     * [START_TIME] = Tidligste tidspunktet for reservasjon
     * [END_TIME] = Seneste tidspunktet for en reservasjon
     * [MAX_HOUR_RESERVATION] = Maks antall timer en reservasjon kan være
     * [LAST_NUM_WITH_PREFIX_ZERO] = Hjelpe konstant for tids-listen med string verdier (ex. 08:00, 09:00)
     */
    companion object {
        const val START_TIME = 8
        const val END_TIME = 23
        const val MAX_HOUR_RESERVATION = 4
        const val LAST_NUM_WITH_PREFIX_ZERO = 9
    }



    // En bakgrunnsjobb. Konseptuelt er en job en kansellerbar ting med en livssyklus.
    private var viewModelJob = Job()
    // Denne utsenderen er optimalisert for å utføre CPU-intensivt arbeid utenfor hovedtråden.
    // Eksempler på brukstilfeller inkluderer sortering av en liste og parsing av JSON.
    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    /**
     * Variablene under følger encapsulation av LiveData
     *
     * Data i et MutableLiveData-objekt kan endres, som navnet tilsier. Inne i ViewModel skal dataene være redigerbare, så de bruker MutableLiveData.
     * Data i et LiveData-objekt kan leses, men ikke endres. Fra utsiden av ViewModel skal data være lesbare,
     * men ikke redigerbare, så dataene skal eksponeres som LiveData.
     */

    // Valgt start-tid (brukes for reservasjon)
    private val _startTime = MutableLiveData<String>()
    val startTime: LiveData<String>
        get() = _startTime

    // Liste med start-tid
    private val _startTimeList = MutableLiveData<List<String>>()
    val startTimeList: LiveData<List<String>>
        get() = _startTimeList

    // Valgt slutt-tid (brukes for reservasjon)
    private val _endTime = MutableLiveData<String>()
    val endTime: LiveData<String>
        get() = _endTime

    // Liste med slutt-tid
    private val _endTimeList = MutableLiveData<List<String>>()
    val endTimeList: LiveData<List<String>>
        get() = _endTimeList

    // Liste med ledige rom, data hentet fra API kall.
    private val _vacantRoomList = MutableLiveData<List<Room>>()
    val vacantRoomList: LiveData<List<Room>>
        get() = _vacantRoomList

    // Valgt dato (brukes for reservasjon)
    private val _selectedDate = MutableLiveData<String>()
    val selectedDate: LiveData<String>
        get() = _selectedDate

    // Liste med grupper, data hentet fra API kall
    private val _groups = MutableLiveData<List<GroupLeaderGroup>>()
    val groups: LiveData<List<GroupLeaderGroup>>
        get() = _groups

    // Gruppe navn
    private val _groupName = MutableLiveData<String>()
    val groupName: LiveData<String>
        get() = _groupName

    // Gruppe id (brukes for reservasjon)
    private val _groupId = MutableLiveData<Int>()
    val groupId: LiveData<Int>
        get() = _groupId


    // Rom-navn
    private val _roomName = MutableLiveData<String>()
    val roomName: LiveData<String>
        get() = _roomName

    // Rom id (brukes for reservasjon)
    private val _roomId = MutableLiveData<Int>()
    val roomId: LiveData<Int>
        get() = _roomId

    // Api status (brukes for å sette status bilde hvis man ikke har kontakt med API)
    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status

    // Bruker reservasjoner, data hentet fra API kall
    private val _userReservation = MutableLiveData<List<UserReservation>>()
    val userReservations: LiveData<List<UserReservation>>
        get() = _userReservation


    // Navigation
    private val _navigateToMyReservations = MutableLiveData<Boolean>()
    val navigateToMyReservations: LiveData<Boolean>
        get() = _navigateToMyReservations

    private val _navigateToReservation = MutableLiveData<Boolean>()
    val navigateToReservation: LiveData<Boolean>
        get() = _navigateToReservation

    // Snackbar
    private val _showSnackBarEvent = MutableLiveData<Boolean>()
    val showSnackBarEvent: LiveData<Boolean>
        get() = _showSnackBarEvent


    /**
     * Brukes i [StartTimeDialogFragment] for å hente start-tid
     */
    fun startTimeSelected(item: String) {
        _startTime.value = item
    }

    /**
     * Brukes i [EndTimeDialogFragment] for å hente slutt-tid,
     * og hente ut ledige rom basert på start og slutt-tid.
     */
    fun endTimeSelected(item: String) {
        _endTime.value = item
        // API kall
        getVacantRooms(_selectedDate.value.toString(), _startTime.value.toString(), _endTime.value.toString())
    }

    /**
     * Brukes i [RoomDialogFragment] for å hente ut romnavn/id
     */
    fun roomNumberSelected(item: Room) {
        _roomName.value = item.name
        _roomId.value = item.id
    }

    /**
     * Brukes i [GroupSelectDialogFragment] for å hente ut gruppenavn/id
     */
    fun selectedGroupId(group: GroupLeaderGroup) {
        _groupName.value = group.groupName
        _groupId.value = group.id
    }


    /**
     * Forhindre at man åpner dialogene når man trykker på knappene i [ReservationFragment]
     * når det ikke er data i listene.
     */
    private fun clearLists() {
        _startTimeList.value = null
        _endTimeList.value = null
        _vacantRoomList.value = null
    }


    /**
     * Henter ledige rom, get request via [GroupFinderApi].
     * Brukes når man setter sluttiden.
     * Baserers på dato, start- og slutt-tid.
     */
    private fun getVacantRooms(date: String, start: String, end: String) {
        // Kjører på coroutine
        coroutineScope.launch {
            val getVacantRoomsDeferred = GroupFinderApi.retrofitService.getVacantRoomsAsync(date = date, start = start, end = end)

            try {
                val listResult = getVacantRoomsDeferred.await()

                val msg = listResult.message
                println(msg)

                // Setter listen med data
                _vacantRoomList.value = listResult.vacantRooms

            } catch (e: Exception) {
                e.printStackTrace()
                println(e.message)
            }
        }
    }


    // Query til db, henter innlogged bruker sine grupper (der han/hun er leder)

    /**
     * Henter gruppene, get request via [GroupFinderApi].
     * Brukes i [ReservationFragment] når man skal velge gruppe for reservasjon.
     */
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


    /**
     * Reserverer rom.
     * Brukes i [ReservationFragment] når man trykker på knapp for reservasjon.
     * Henter data basert på input (LiveData variablene)
     * Sender post request til API med reservasjon via [GroupFinderApi].
     */
    fun onReserveRoom() {

        // Bygger opp query params
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
        // Reset data i listene
        clearLists()
        // Sender ok signal til snackbar
        _showSnackBarEvent.value = true
    }


    /**
     * Henter start-tid listen.
     * Brukes i [StartTimeDialogFragment].
     * Data for denne listen er forskjellig fra dagens dato og en dato i fremtiden, er det dagens dato
     * kalkulerer setDateStartTime tidspunkt basert på nåværende klokkeslett og utover.
     *
     * @return ArrayList av klokkeslett i String
     */
    fun getStartTimeList() : ArrayList<String> {
        return _startTimeList.value as ArrayList<String>
    }

    /**
     * Henter slutt-tid listen.
     * Brukes i [EndTimeDialogFragment].
     *
     * @return ArrayList av klokkeslett i String
     */
    fun getEndTimeList() : ArrayList<String> {
        // Henter substring av start-/slutt-tiden for å loop'e gjennom klokkeslettene og sette slutt listen
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


    /**
     * Setter dato og start-tid basert på input fra DatePicker (Long verdi)
     * Brukes i [ReservationFragment].
     *
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun setDateStartTime(date: Long){
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)

        // Dato som kommer inn
        val dateInput = Date(date)
        val dateInputFormat = format.format(dateInput)
        // Dagens dato (brukes for sjekk)
        val currentDate = Date()
        val currentDateFormat = format.format(currentDate)

        _selectedDate.value = dateInputFormat

        /**
         * Når dato ikke er dagens dato, fyll listen fra [START_TIME] til [END_TIME] - [MAX_HOUR_RESERVATION]
         */
        if (currentDateFormat != dateInputFormat){
            _startTimeList.value = ArrayList()

            for (i in START_TIME..END_TIME - MAX_HOUR_RESERVATION) {
                if (i <= LAST_NUM_WITH_PREFIX_ZERO)
                    (_startTimeList.value as ArrayList<String>).add("0$i:00")
                else
                    (_startTimeList.value as ArrayList<String>).add("$i:00")
            }

        }
        /**
         * Når dato er dagens dato, hent nærmeste time (hjelpe funksjon)
         * Fyll listen fra nærmesteTime til [END_TIME] - [MAX_HOUR_RESERVATION]
         */
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


    /**
     * Henter alle reservasjonene til brukeren.
     * Brukes i [ReservationFragment] og [UserReservationFragment].
     * Oppdateres når man navigerer mellom fragmentene.
     */
    fun getReservations(email: String) {
        coroutineScope.launch {

            val getReservationsDeferred = GroupFinderApi.retrofitService.getUserReservationsAsync(email)

            try {
                _status.value = ApiStatus.LOADING
                val listResult = getReservationsDeferred.await()
                println(listResult)
                _status.value = ApiStatus.DONE
                _userReservation.value = listResult.userReservations

            } catch (e: Exception) {
                _status.value = ApiStatus.ERROR
                e.printStackTrace()
                _userReservation.value = ArrayList()
            }

        }
    }

    /**
     * @param now Henter nærmeste time basert på dette
     */
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

    /**
     * Skulle egentlig åpne et nytt view for å redigere reservasjonen man klikker på.
     */
    fun displayEditReservation(userReservation: UserReservation) {
        println(userReservation)
    }

    fun doneNavigating() {
        _navigateToReservation.value = false
    }

    fun onNavigateToReservation() {
        _navigateToReservation.value = true
    }

    fun onReservationComplete() {
        _navigateToMyReservations.value = false
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
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}


