package com.example.groupfinder.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.groupfinder.calendar.adapters.UserReservationListAdapter
import com.example.groupfinder.databinding.UserReservationsFragmentBinding
import com.example.groupfinder.util.Constants
import com.example.groupfinder.util.PreferenceProvider

/**
 * [UserReservationFragment] inneholder en liste over reservasjonen(e) til den/de gruppen(e) man er medlem av.
 * Og lar deg navigere til [ReservationFragment]
 * Bygget opp med [Fragment] og data-binding. Benytter seg av en delt viewmodel.
 *
 * @author Anders Olai Pedersen - 225280
 */
class UserReservationFragment : Fragment() {

    private val vms: ReservationViewModelShared by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        val binding = UserReservationsFragmentBinding.inflate(inflater)


        binding.lifecycleOwner = this
        binding.viewModel = vms

        // SharedPreference, bruker epost. Brukes til å hente ut reservasjonene når man åpner fragment
        val pref = this.context?.let { PreferenceProvider(it) }
        val userEmail: String? = pref?.getEmailPreference(Constants.KEY_EMAIL)


        // Binding til xml, knapp som sender deg til reservasjon
        binding.navToReservationButton.setOnClickListener {
            vms.onNavigateToReservation()
        }

        // Observerer en boolean i ViewModel, er den satt til true blir du navigert tilbake (etter reservasjon er ok)
        vms.navigateToReservation.observe(viewLifecycleOwner, Observer { e ->
            if (e) {
                e?.let {
                    this.findNavController().navigate(
                        UserReservationFragmentDirections.actionMyReservationFragmentToReservationFragment())
                    vms.doneNavigating()
                }
            }
        })

        // Binding med xml -> Adapter til listen, sender info til viewModel.
        // Adapteret ligger i util.BindingAdapters.kt
        binding.userReservationList.adapter = UserReservationListAdapter(UserReservationListAdapter.OnClickListener {
            vms.displayEditReservation(it)
        })


        // Henter reservasjonene (API kall i viewmodel)
        vms.getReservations(userEmail.toString())

        return binding.root

    }
}
