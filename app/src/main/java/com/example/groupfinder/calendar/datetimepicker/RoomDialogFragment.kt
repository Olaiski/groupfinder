package com.example.groupfinder.calendar.datetimepicker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.groupfinder.R
import com.example.groupfinder.databinding.ListDialogFragmentBinding

class RoomDialogFragment : DialogFragment() {


    private val reservationViewModelShared: ReservationViewModelShared by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: ListDialogFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.list_dialog_fragment, container, false)

        binding.timeViewModelShared = reservationViewModelShared

        binding.lifecycleOwner = this

//        val roomList = binding.groupList
//        val roomString = reservationViewModelShared.setRoomNumberList()
//        val arrayAdapter = this.context?.let { ArrayAdapter(it, R.layout.item_for_list_text_view, R.id.text_view_item, roomString) }


//        roomList.adapter = arrayAdapter
//
//        // TODO: 20/09/2020 Bruke _, _, position, id -> for å hente ut?
//        roomList.setOnItemClickListener { _, _, position, _ ->
//            val selectedRoomId = arrayAdapter?.getItem(position)
//            reservationViewModelShared.roomNumberSelected(selectedRoomId.toString())
//
//            this.dismiss()
//        }





        return binding.root
    }
}

