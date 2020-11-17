package com.example.groupfinder.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import com.example.groupfinder.R
import com.example.groupfinder.calendar.datetimepicker.*
import com.example.groupfinder.databinding.ReservationFragmentBinding
import com.example.groupfinder.util.PreferenceProvider
import com.example.groupfinder.util.Constants
import com.example.groupfinder.util.onDateSet
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import java.util.*


class ReservationFragment : Fragment() {

    private val reservationViewModelShared: ReservationViewModelShared by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle? ) : View? {

        val binding: ReservationFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.reservation_fragment, container, false)


//        val application = requireNotNull(this.activity).application
//        val dataSource = GroupFinderDatabase.getInstance(application).groupFinderDatabaseDao
//        val viewModelFactory = ReservationViewModelFactory(dataSource, application)
//        val reservationViewModel = ViewModelProvider(this, viewModelFactory).get(ReservationViewModel::class.java)
//        binding.calenderViewModel = reservationViewModel


        val pref = this.context?.let { PreferenceProvider(it) }
        val userEmail: String? = pref?.getEmailPreference(Constants.KEY_EMAIL)
        println(userEmail)


//        if (userEmail != null) {
//            reservationViewModelShared.setGroupList(userEmail)
//        }


        binding.lifecycleOwner = this




        // Start time dialog / button
        val startTimeInput = binding.startTimeInput
        val startTimeDialog = StartTimeDialogFragment()

        startTimeInput.setOnClickListener {
            startTimeDialog.show(parentFragmentManager, startTimeInput.toString())
        }
        reservationViewModelShared.startTime.observe(viewLifecycleOwner, { item ->
            startTimeInput.text = item.toString()
        })


        // End time dialog / button
        val endTimeInput = binding.endTimeInput
        val endTimeDialog = EndTimeDialogFragment()

        endTimeInput.setOnClickListener {
            endTimeDialog.show(parentFragmentManager, endTimeInput.toString())
        }

        reservationViewModelShared.endTime.observe(viewLifecycleOwner, { item ->
            endTimeInput.text = item.toString()
        })


        // Room number dialog / button
        val roomInput = binding.roomNumberInput
        val roomDialog = RoomDialogFragment()

        roomInput.setOnClickListener {
            roomDialog.show(parentFragmentManager, roomInput.toString())
        }

        reservationViewModelShared.roomNumber.observe(viewLifecycleOwner, {item ->
            roomInput.text = item.toString()
        })


        // Calendar, DatePickerDialog / Button
        val dateInput = binding.dateInput
        val builder: MaterialDatePicker.Builder<Long> = MaterialDatePicker.Builder.datePicker()
        val currentTimeInMillis = Calendar.getInstance().timeInMillis
        builder.setSelection(currentTimeInMillis)
        builder.setTitleText("Select a Date")

        val picker: MaterialDatePicker<*> = builder.build()
        dateInput.setOnClickListener {
            picker.show(parentFragmentManager, picker.toString())
        }


        picker.addOnPositiveButtonClickListener {
            dateInput.text = picker.headerText

        }


        // Group dialog / button
        val groupInput = binding.groupInput
        val groupDialog = GroupSelectDialogFragment()
        groupInput.setOnClickListener {
            reservationViewModelShared.getGroups(userEmail.toString())
            groupDialog.show(parentFragmentManager, groupInput.toString())
        }

//
//        reservationViewModelShared.groupName.observe(viewLifecycleOwner, { item ->
//            groupInput.text = item.toString()
//        })


        binding.reserveButton.setOnClickListener {
            reservationViewModelShared.onReserveRoom(
                startTime = binding.startTimeInput.text.toString(),
                endTime = binding.endTimeInput.text.toString(),
                date = onDateSet(binding.dateInput.text.toString()),
                roomNumber = binding.roomNumberInput.text.toString(),
                groupName = binding.groupInput.text.toString()
            )

        }



        reservationViewModelShared.showSnackBarEvent.observe(viewLifecycleOwner, {
            if (it == true) {
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    "Reservation OK!",
                    Snackbar.LENGTH_LONG
                ).show()
                reservationViewModelShared.doneShowingSnackbar()
            }
        })

//        reservationViewModelShared.navigateToMyReservations.observe(viewLifecycleOwner, { e ->
//            if (e) {
//                this.findNavController().navigate(
//                    ReservationFragmentDirections.actionReservationFragmentToMyReservationFragment())
//                reservationViewModelShared.onReservationComplete()
//            }
//        })





        return binding.root
    }








}

