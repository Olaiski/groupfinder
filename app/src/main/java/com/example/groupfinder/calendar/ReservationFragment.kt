package com.example.groupfinder.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.groupfinder.R
import com.example.groupfinder.calendar.datetimepicker.EndTimeDialogFragment
import com.example.groupfinder.calendar.datetimepicker.StartTimeDialogFragment
import com.example.groupfinder.calendar.datetimepicker.ReservationViewModelShared
import com.example.groupfinder.calendar.datetimepicker.RoomDialogFragment
import com.example.groupfinder.database.GroupFinderDatabase

import com.example.groupfinder.databinding.ReservationFragmentBinding


class ReservationFragment : Fragment() {

    private val reservationViewModelShared: ReservationViewModelShared by activityViewModels()



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle? ) : View? {

        val binding: ReservationFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.reservation_fragment, container, false)


        val application = requireNotNull(this.activity).application

        val dataSource = GroupFinderDatabase.getInstance(application).groupFinderDatabaseDao

        val viewModelFactory = ReservationViewModelFactory(dataSource, application)

        val calendarViewModel = ViewModelProvider(this, viewModelFactory).get(ReservationViewModel::class.java)


        binding.calenderViewModel = calendarViewModel

        binding.lifecycleOwner = this



        // Start time dialog / button
        val startTimeInput = binding.startTimeInput
        val startTimeDialog = StartTimeDialogFragment()

        binding.startTimeInput.setOnClickListener {
            startTimeDialog.show(parentFragmentManager, "start")
        }
        reservationViewModelShared.startTime.observe(viewLifecycleOwner, { item ->
            startTimeInput.text = item.toString()
        })


        // End time dialog / button
        val endTimeInput = binding.endTimeInput
        val endTimeDialog = EndTimeDialogFragment()

        binding.endTimeInput.setOnClickListener {
            endTimeDialog.show(parentFragmentManager, "end")
        }

        reservationViewModelShared.endTime.observe(viewLifecycleOwner, { item ->
            endTimeInput.text = item.toString()
        })


        // Room number dialog / button
        val roomInput = binding.roomNumberInput
        val roomDialog = RoomDialogFragment()

        binding.roomNumberInput.setOnClickListener {
            roomDialog.show(parentFragmentManager, "room")
        }

        reservationViewModelShared.roomNumber.observe(viewLifecycleOwner, {item ->
            roomInput.text = item.toString()
        })



        return binding.root
    }



}

