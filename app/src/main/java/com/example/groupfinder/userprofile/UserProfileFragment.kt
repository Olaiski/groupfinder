package com.example.groupfinder.userprofile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.groupfinder.R
import com.example.groupfinder.database.GroupFinderDatabase
import com.example.groupfinder.databinding.UserProfileFragmentBinding

class UserProfileFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        return super.onCreateView(inflater, container, savedInstanceState)

        val binding: UserProfileFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.user_profile_fragment, container, false)

        val application = requireNotNull(this.activity).application

        val dataSource = GroupFinderDatabase.getInstance(application).groupFinderDatabaseDao

        val viewModelFactory = UserProfileViewModelFactory(dataSource, application)

        val userProfileViewModel =
            ViewModelProvider(this, viewModelFactory).get(UserProfileViewModel::class.java)

        binding.userProfileViewModel = userProfileViewModel

        binding.lifecycleOwner = this


        return binding.root
    }

    private fun showWelcomeMessage() {
        // ...
    }
}