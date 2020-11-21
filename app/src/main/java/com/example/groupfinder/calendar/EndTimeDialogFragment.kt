package com.example.groupfinder.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.groupfinder.R
import com.example.groupfinder.databinding.TimeDialogFragmentBinding

/**
 * [EndTimeDialogFragment] inneholder en liste der man velger slutt-tiden til en reservasjon.
 * Bygget opp med [DialogFragment] og data-binding. Benytter seg av en delt viewmodel.
 *
 * @author Anders Olai Peders - 225280
 */

class EndTimeDialogFragment : DialogFragment() {

    private val reservationViewModelShared: ReservationViewModelShared by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: TimeDialogFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.time_dialog_fragment, container, false)



        binding.timeViewModelShared = reservationViewModelShared

        binding.lifecycleOwner = this


        // Binding til arrayAdapter, fyller listen med tidspunktene
        // Henter tidspunktene (basert på valgt start-tid)
        val timeList = binding.timeList
        val timeString = reservationViewModelShared.getEndTimeList()
        val arrayAdapter = this.context?.let { ArrayAdapter(it, R.layout.item_for_list_text_view, R.id.text_view_item, timeString) }

        timeList.adapter = arrayAdapter


        // ClickListener, setter tidspunktet
        timeList.setOnItemClickListener { _, _, position, _ ->
            val selectedEndTime = arrayAdapter?.getItem(position)
            reservationViewModelShared.endTimeSelected(selectedEndTime.toString())
            this.dismiss()
        }

        return binding.root
    }
}