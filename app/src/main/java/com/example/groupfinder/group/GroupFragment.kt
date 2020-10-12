package com.example.groupfinder.group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.groupfinder.databinding.GroupFragmentBinding


/**
 * This [Fragment] shows the detailed information about a selected group.
 * It sets this information in the [GroupViewModel], which it gets as a Parcelable property
 * through Jetpack Navigation's safeArgs
 */
class GroupFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireNotNull(activity).application
        val binding = GroupFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        val group = GroupFragmentArgs.fromBundle(requireArguments()).selectedGroup


        val viewModelFactory = GroupViewModelFactory(group, application)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(GroupViewModel::class.java)
        binding.groupViewModel = viewModel


        /**
         * GroupMember adapter. Functional clicklistener. Not in use at the moment.
         * Implemented for later use.
         */
        binding.groupMembersList.adapter = StudentMembersListAdapter(StudentMembersListAdapter.OnClickListener {
            viewModel.displayGroupMembers(it)
        })


        return binding.root
    }

}