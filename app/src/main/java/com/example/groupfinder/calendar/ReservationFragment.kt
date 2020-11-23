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
import com.example.groupfinder.databinding.ReservationFragmentBinding
import com.example.groupfinder.util.PreferenceProvider
import com.example.groupfinder.util.Constants
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import java.util.*

/**
 * Lar brukeren reservere rom.
 * [ReservationFragment] er bygget opp med en [MaterialDatePicker] som lar deg velge en dato.
 *  Og oppretter [StartTimeDialogFragment], [EndTimeDialogFragment] og [GroupSelectDialogFragment] når man skal velge tidspunkt og hvilken
 *  gruppe det gjelder.
 *
 *  @author Anders Olai Pedersen - 225280
 */
class ReservationFragment : Fragment() {

    private val vms: ReservationViewModelShared by activityViewModels()


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle? ) : View? {

        val binding: ReservationFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.reservation_fragment, container, false)

        // SharedPreference, bruker epost. Brukes til å hente ut gruppene når man åpner fragment
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
            // Sender dato til viewModel
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
        // Setter teksten til knappen med tidspunkt
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
        // Setter teksten til knappen med tidspunkt
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
        // Setter teksten til knappen med romnavn
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
        // Setter teksten til knappen med gruppenavn
        vms.groupName.observe(viewLifecycleOwner, { item ->
            groupInput.text = item.toString()
        })


        // Reserverer rom
        binding.reserveButton.setOnClickListener {
            val dateTxt = dateInput.text.toString()
            val startTxt = startTimeInput.text.toString()
            val endTxt = endTimeInput.text.toString()
            val roomNameTxt = roomInput.text.toString()
            val groupNameTxt = groupInput.text.toString()

            if (dateTxt.isEmpty()) {
                showSnackbar("Missing date")
                return@setOnClickListener
            }

            if (startTxt.isEmpty()) {
                showSnackbar("Missing start time")
                return@setOnClickListener
            }

            if (endTxt.isEmpty()) {
                showSnackbar("Missing end time")
                return@setOnClickListener
            }

            if (roomNameTxt.isEmpty()) {
                showSnackbar("Missing room")
                return@setOnClickListener
            }

            if (groupNameTxt.isEmpty()) {
                showSnackbar("Missing group")
                return@setOnClickListener
            }

            // API kall
            vms.onReserveRoom()
        }


        // Observerer en boolean, blir satt true når reservasjon er ok
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

        /**
         * Navigerer tilbake til [UserReservationFragment] og oppdaterer reservasjonslisten
         */
        vms.navigateToMyReservations.observe(viewLifecycleOwner, { e ->
            if (e) {
                vms.getReservations(userEmail.toString())
                this.findNavController().navigate(
                    ReservationFragmentDirections.actionReservationFragmentToMyReservationFragment())
                vms.onReservationComplete()
            }
        })



        return binding.root
    }

    /**
     * Hjelpemetode for å opprette snackbar med informasjon,
     * brukes i sjekk av data når man reserverer rom
     */
    private fun showSnackbar(message: String) {
        Snackbar.make(
            requireActivity().findViewById(android.R.id.content),
            message,
            Snackbar.LENGTH_LONG
        ).show()
    }

    override fun onDetach() {
        super.onDetach()
        vms.clearData()
    }
}

