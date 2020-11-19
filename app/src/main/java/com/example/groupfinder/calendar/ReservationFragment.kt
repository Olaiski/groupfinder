package com.example.groupfinder.calendar

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.groupfinder.R
import com.example.groupfinder.calendar.datetimepicker.*
import com.example.groupfinder.databinding.ReservationFragmentBinding
import com.example.groupfinder.util.PreferenceProvider
import com.example.groupfinder.util.Constants
import com.example.groupfinder.util.onDateSet
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.*
import java.util.*


class ReservationFragment : Fragment() {

    private val vms: ReservationViewModelShared by activityViewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle? ) : View? {

        val binding: ReservationFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.reservation_fragment, container, false)


        val pref = this.context?.let { PreferenceProvider(it) }
        val userEmail: String? = pref?.getEmailPreference(Constants.KEY_EMAIL)



        binding.lifecycleOwner = this




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
            vms.setDateStartTime(picker.selection as Long)
            binding.startTimeInput.text = ""
            binding.endTimeInput.text = ""
        }


        // Start time dialog / button
        val startTimeInput = binding.startTimeInput
        val startTimeDialog = StartTimeDialogFragment()

        startTimeInput.setOnClickListener {
            if (vms.startTimeList.value == null) {
                showSnackbar("Choose a date")
                return@setOnClickListener
            }
            startTimeDialog.show(parentFragmentManager, startTimeInput.toString())


        }

        vms.startTime.observe(viewLifecycleOwner, { item ->
            startTimeInput.text = item.toString()
        })



        // End time dialog / button
        val endTimeInput = binding.endTimeInput
        val endTimeDialog = EndTimeDialogFragment()

        endTimeInput.setOnClickListener {
            if (vms.startTimeList.value == null) {
                showSnackbar("Set start time")
                return@setOnClickListener
            }
            endTimeDialog.show(parentFragmentManager, endTimeInput.toString())
        }

        vms.endTime.observe(viewLifecycleOwner, { item ->
            endTimeInput.text = item.toString()
        })



        // Room number dialog / button
        val roomInput = binding.roomNumberInput
        val roomDialog = RoomDialogFragment()

        roomInput.setOnClickListener {
            if (vms.startTimeList.value == null) {
                showSnackbar("Choose Date/Start-/End-time")
                return@setOnClickListener
            }
            roomDialog.show(parentFragmentManager, roomInput.toString())
        }

        vms.roomName.observe(viewLifecycleOwner, { item ->
            roomInput.text = item.toString()
        })


        // Group dialog / button
        val groupInput = binding.groupInput
        val groupDialog = GroupSelectDialogFragment()
        groupInput.setOnClickListener {
            vms.getGroups(userEmail.toString())
            groupDialog.show(parentFragmentManager, groupInput.toString())
        }

        vms.groupName.observe(viewLifecycleOwner, { item ->
            groupInput.text = item.toString()
        })


        binding.reserveButton.setOnClickListener {
            vms.onReserveRoom()
        }


        vms.showSnackBarEvent.observe(viewLifecycleOwner, {
            if (it == true) {
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    "Reservation OK!",
                    Snackbar.LENGTH_LONG
                ).show()
                vms.doneShowingSnackbar()
            }
        })

        vms.navigateToMyReservations.observe(viewLifecycleOwner, { e ->
            if (e) {
                this.findNavController().navigate(
                    ReservationFragmentDirections.actionReservationFragmentToMyReservationFragment())
                vms.onReservationComplete()
            }
        })

        return binding.root
    }


    private fun showSnackbar(message: String) {
        Snackbar.make(
            requireActivity().findViewById(android.R.id.content),
            message,
            Snackbar.LENGTH_LONG
        ).show()
    }

    override fun onDetach() {
        super.onDetach()
        vms.startTimeSelected("")
        vms.endTimeSelected("")
        vms.groupSelected("")
    }
}

