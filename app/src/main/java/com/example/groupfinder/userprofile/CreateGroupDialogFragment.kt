package com.example.groupfinder.userprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.groupfinder.R
import com.example.groupfinder.database.GroupFinderDatabase
import com.example.groupfinder.databinding.CreateGroupDialogFragmentBinding
import com.example.groupfinder.network.models.Group
import com.example.groupfinder.network.models.PostGroup
import com.google.android.material.snackbar.Snackbar

class CreateGroupDialogFragment : DialogFragment() {

    private val viewModel: CreateGroupViewModel by lazy {
        ViewModelProvider(this).get(CreateGroupViewModel::class.java)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle? ): View? {

        val binding = CreateGroupDialogFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel


        binding.createGroupCancelButton.setOnClickListener {
            this.dismiss()
        }


        binding.createGroupCreateButton.setOnClickListener {
            val group = PostGroup(

                groupName = binding.courseGroupNameInputText.text.toString(),
                description = binding.courseDescriptionInputText.text.toString(),
                courseCode = binding.courseCodeInputText.text.toString(),
                location = binding.locationInputText.text.toString()
                )

            viewModel.onCreateGroup(group)

//            this.dismiss()
        }

        viewModel.showSnackBarEvent.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    "New group created!",
                    Snackbar.LENGTH_LONG
                ).show()
                viewModel.doneShowingSnackbar()
            }
        })





        return binding.root
    }
}