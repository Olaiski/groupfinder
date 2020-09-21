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



class StartTimeDialogFragment : DialogFragment() {


    private val reservationViewModelShared: ReservationViewModelShared by activityViewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle? ): View? {
        val binding: ListDialogFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.list_dialog_fragment, container, false)

//        val viewModel = ViewModelProvider(this).get(TimeViewModelShared::class.java)

        binding.timeViewModelShared = reservationViewModelShared


        binding.lifecycleOwner = this



//         When the content for your layout is dynamic or not pre-determined, you can use a layout that subclasses AdapterView to populate the layout with views at runtime.
//         A subclass of the AdapterView class uses an Adapter to bind data to its layout.
//         The Adapter behaves as a middleman between the data source and the AdapterView layout—the Adapter retrieves the data
//         (from a source such as an array or a database query) and converts each entry into a view that can be added into the AdapterView layout.
        val timeList = binding.itemList
        val timeString = reservationViewModelShared.setTimeList()
        val arrayAdapter = this.context?.let { ArrayAdapter(it, R.layout.item_for_list_text_view, R.id.text_view_item, timeString) }

        timeList.adapter = arrayAdapter

        timeList.setOnItemClickListener { _, _, position, _ ->
            val selectedStartTime = arrayAdapter?.getItem(position)
            reservationViewModelShared.startTimeSelected(selectedStartTime.toString())



            this.dismiss()
        }




        return binding.root
    }

}