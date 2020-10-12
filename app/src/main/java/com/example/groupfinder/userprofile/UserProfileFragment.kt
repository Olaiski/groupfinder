package com.example.groupfinder.userprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.groupfinder.databinding.UserProfileFragmentBinding
import kotlinx.android.synthetic.main.user_profile_fragment.*


/**
 *  [UserProfileFragment] :
 *   - Displays the user profile information
 *   - Let's user create a new group
 *   - Displays information about all the groups related to the user
 */
class UserProfileFragment : Fragment() {


//    private val viewModel: UserProfileViewModel by activityViewModels()

    private val viewModel: UserProfileViewModel by lazy {
        ViewModelProvider(this).get(UserProfileViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding = UserProfileFragmentBinding.inflate(inflater)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val userId = viewModel.sId


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






        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        user_group_list.adapter = null
    }

    private fun showWelcomeMessage() {
        // ...
    }
}