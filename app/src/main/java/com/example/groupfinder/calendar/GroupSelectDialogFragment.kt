package com.example.groupfinder.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.groupfinder.R
import com.example.groupfinder.calendar.adapters.GroupLeaderListAdapter
import com.example.groupfinder.databinding.ListDialogFragmentBinding


/**
 * [GroupSelectDialogFragment] inneholder en liste der man velger gruppen til en reservasjon.
 * Bygget opp med [DialogFragment] og data-binding. Benytter seg av en delt viewmodel.
 *
 * @author Anders Olai Peders - 225280
 */
class GroupSelectDialogFragment : DialogFragment() {

    private val reservationViewModelShared: ReservationViewModelShared by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        val binding: ListDialogFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.list_dialog_fragment, container, false)

        binding.timeViewModelShared = reservationViewModelShared

        binding.lifecycleOwner = this

        // Binding med xml -> Adapter til listen, sender info til viewModel.
        // Adapteret ligger i util.BindingAdapters.kt
        binding.groupLeaderList.adapter = GroupLeaderListAdapter(GroupLeaderListAdapter.OnClickListener {
            reservationViewModelShared.selectedGroupId(it)
            this.dismiss()
        })

        return binding.root
    }
}