package com.example.groupfinder.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.groupfinder.R
import com.example.groupfinder.calendar.adapters.RoomListAdapter
import com.example.groupfinder.databinding.RoomDialogFragmentBinding


/**
 * [RoomDialogFragment] inneholder en liste der man velger gruppen til en reservasjon.
 *  Bygget opp med [DialogFragment] og data-binding. Benytter seg av en delt viewmodel.
 *
 * @author Anders Olai Pedersen - 225280
 */
class RoomDialogFragment : DialogFragment() {


    private val reservationViewModelShared: ReservationViewModelShared by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: RoomDialogFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.room_dialog_fragment, container, false)


        binding.vms = reservationViewModelShared

        binding.lifecycleOwner = this

        // Binding med xml -> Adapter til listen, sender info til viewModel.
        // Adapteret ligger i util.BindingAdapters.kt
        binding.roomList.adapter = RoomListAdapter(RoomListAdapter.OnClickListener {
            reservationViewModelShared.roomNumberSelected(it)
            this.dismiss()
        })


        return binding.root
    }
}

