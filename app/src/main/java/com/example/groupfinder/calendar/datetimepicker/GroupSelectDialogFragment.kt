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
import com.example.groupfinder.userprofile.GroupListAdapter
import com.example.groupfinder.util.Constants
import com.example.groupfinder.util.PreferenceProvider

class GroupSelectDialogFragment : DialogFragment() {

    private val reservationViewModelShared: ReservationViewModelShared by activityViewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        val binding: ListDialogFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.list_dialog_fragment, container, false)

        binding.timeViewModelShared = reservationViewModelShared

        binding.lifecycleOwner = this

        binding.groupLeaderList.adapter = GroupLeaderListAdapter(GroupLeaderListAdapter.OnClickListener {
            reservationViewModelShared.selectedGroupId(it)
            this.dismiss()
        })

        return binding.root
    }
}