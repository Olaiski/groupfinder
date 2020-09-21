package com.example.groupfinder.calendar;

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.groupfinder.R
import com.example.groupfinder.database.GroupFinderDatabase
import com.example.groupfinder.databinding.MyReservationsFragmentBinding

class MyReservationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: MyReservationsFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.my_reservations_fragment, container, false)

        val application = requireNotNull(this.activity).application
        val dataSource = GroupFinderDatabase.getInstance(application).groupFinderDatabaseDao

        val viewModelFactory = MyReservationViewModelFactory(dataSource, application)

        val myReservationViewModel = ViewModelProvider(this, viewModelFactory).get(MyReservationViewModel::class.java)


        binding.myReservationViewModel = myReservationViewModel

        binding.lifecycleOwner = this


        // TODO: 21/09/2020 Dummy data.. Fjernes.
        val resString = myReservationViewModel.setDummyRes()

        binding.navToReservationButton.setOnClickListener {
            myReservationViewModel.setNavigation(true)
        }

        myReservationViewModel.navigateToReservation.observe(viewLifecycleOwner, Observer { e ->
            if (e) {
                e?.let {
                    this.findNavController().navigate(
                        MyReservationFragmentDirections.actionMyReservationFragmentToReservationFragment())

                    myReservationViewModel.doneNavigating()
                }
            }
        })




        return binding.root
    }
}
