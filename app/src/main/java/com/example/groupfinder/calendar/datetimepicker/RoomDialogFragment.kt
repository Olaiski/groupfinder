package com.example.groupfinder.calendar.datetimepicker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.groupfinder.R
import com.example.groupfinder.databinding.RoomDialogFragmentBinding

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


        binding.roomList.adapter = RoomListAdapter(RoomListAdapter.OnClickListener {
            reservationViewModelShared.roomNumberSelected(it)
            this.dismiss()
        })


        return binding.root
    }
}

