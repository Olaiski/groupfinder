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

class GroupSelectDialogFragment : DialogFragment() {

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



        val groupList = binding.itemList
        val groupString = reservationViewModelShared.setGroupList()
        val arrayAdapter = this.context?.let { ArrayAdapter(it, R.layout.item_for_list_text_view, R.id.text_view_item, groupString) }


        groupList.adapter = arrayAdapter

        groupList.setOnItemClickListener{ _, _, position, _ ->
            val selectedGroup = arrayAdapter?.getItem(position)
            reservationViewModelShared.groupSelected(selectedGroup.toString())

            this.dismiss()
        }



        return binding.root
    }
}