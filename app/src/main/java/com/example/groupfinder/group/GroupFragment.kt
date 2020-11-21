package com.example.groupfinder.group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.app.Application
import com.example.groupfinder.network.models.Group
import androidx.lifecycle.ViewModelProvider
import com.example.groupfinder.databinding.GroupFragmentBinding


/**
 * Dette [Fragment] viser detaljert informasjon om en valgt gruppe.
 * Den angir denne informasjonen i [GroupViewModel], som den får som en "Parcelable property"
 * gjennom Jetpack Navigations safeArgs.
 *
 * Oppretter [GroupViewModelFactory] fordi vi sender med [Group] og [Application] som parametere.
 *
 * @author Anders Olai Pedersen - 225280
 */
class GroupFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        val application = requireNotNull(activity).application
        val binding = GroupFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this

        // Henter gruppe basert på args
        val group = GroupFragmentArgs.fromBundle(requireArguments()).selectedGroup


        // Factory
        val viewModelFactory = GroupViewModelFactory(group, application)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(GroupViewModel::class.java)


        binding.groupViewModel = viewModel


        /**
         * Når man klikker på en av studentene i gruppen, er det lagt opp til
         * at man kan hente ut informasjon og vise detaljer om den studeten. Men den er ikke i bruk
         * for øyeblikket (men den henter ut info).
         */
        binding.groupMembersList.adapter = StudentMembersListAdapter(StudentMembersListAdapter.OnClickListener {
            viewModel.displayGroupMembers(it)
        })


        return binding.root
    }

}