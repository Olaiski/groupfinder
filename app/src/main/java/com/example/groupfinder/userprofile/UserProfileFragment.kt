package com.example.groupfinder.userprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.groupfinder.databinding.UserProfileFragmentBinding
import com.example.groupfinder.util.Constants
import com.example.groupfinder.util.PreferenceProvider
import kotlinx.android.synthetic.main.user_profile_fragment.*


/**
 *  [UserProfileFragment], bruker en delt viewmodel.
 *   - Viser informasjon om brukerprofilen
 *   - Lar bruker opprette en ny gruppe
 *   - Viser informasjon om alle gruppene som er relatert til brukeren
 *
 *   @author Anders Olai Pedersen
 */
class UserProfileFragment : Fragment() {


    private val viewModel: UserProfileViewModel by activityViewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding = UserProfileFragmentBinding.inflate(inflater)

        val pref = this.context?.let { PreferenceProvider(it) }
        val userEmail: String? = pref?.getEmailPreference(Constants.KEY_EMAIL)
        // Setter synlighet for "bli med" knapp (er man allerede i gruppen skal den ikke vises)
        pref?.putShowButton(Constants.BTN_SHOW, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel


        // Knapp og dialog for å opprette gruppe
        val createGroupDialog = CreateGroupDialogFragment()
        val createGroupButton = binding.createGroupButton

        createGroupButton.setOnClickListener {
            createGroupDialog.show(parentFragmentManager, "cgd")
        }


        // Adapter for gruppene
        binding.userGroupList.adapter = GroupListAdapter(GroupListAdapter.OnClickListener {
            viewModel.displayGroupDetails(it)
        })

        // Navigerer til gruppen med gruppe som param.
        viewModel.navigateToSelectedGroup.observe(viewLifecycleOwner, Observer {
            if ( null != it ){
                this.findNavController().navigate(UserProfileFragmentDirections.showGroupFragment(it))
                viewModel.displayGroupDetailsComplete()
            }
        })

        // Nytt kall på API for å oppdatere listen etter vellykket opprettelse av gruppe
        viewModel.createGroupSuccess.observe(viewLifecycleOwner, Observer {
            if (it) {
                viewModel.getGroups(userEmail.toString())
            }
        })


        viewModel.getGroups(userEmail.toString())

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        user_group_list.adapter = null
    }

}