package com.example.groupfinder.userprofile

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.example.groupfinder.databinding.UserProfileFragmentBinding
import com.example.groupfinder.util.Constants
import com.example.groupfinder.util.PreferenceProvider
import kotlinx.android.synthetic.main.user_profile_fragment.*


/**
 *  [UserProfileFragment] :
 *   - Displays the user profile information
 *   - Let's user create a new group
 *   - Displays information about all the groups related to the user
 */
class UserProfileFragment : Fragment() {


    private val viewModel: UserProfileViewModel by activityViewModels()

//    private val viewModel: UserProfileViewModel by lazy {
//        ViewModelProvider(this).get(UserProfileViewModel::class.java)
//    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding = UserProfileFragmentBinding.inflate(inflater)

        val pref = this.context?.let { PreferenceProvider(it) }
        val userEmail: String? = pref?.getEmailPreference(Constants.KEY_EMAIL)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel


        val createGroupDialog = CreateGroupDialogFragment()
        val createGroupButton = binding.createGroupButton

        createGroupButton.setOnClickListener {
            createGroupDialog.show(parentFragmentManager, "cgd")
        }


        binding.userGroupList.adapter = GroupListAdapter(GroupListAdapter.OnClickListener {
            viewModel.displayGroupDetails(it)
        })

        viewModel.navigateToSelectedGroup.observe(viewLifecycleOwner, Observer {
            if ( null != it ){
                this.findNavController().navigate(UserProfileFragmentDirections.showGroupFragment(it))

                viewModel.displayGroupDetailsComplete()
            }
        })

        viewModel.createGroupSuccess.observe(viewLifecycleOwner, Observer {
            if (it) {
                println("UPF ${viewModel.email.value.toString()}")
                viewModel.getGroups(viewModel.email.value.toString())
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