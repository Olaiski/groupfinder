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
import com.google.android.material.snackbar.Snackbar

class CreateGroupDialogFragment : DialogFragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: CreateGroupDialogFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.create_group_dialog_fragment, container, false)


        val application = requireNotNull(this.activity).application

        val dataSource = GroupFinderDatabase.getInstance(application).groupFinderDatabaseDao

        val viewModelFactory = CreateGroupViewModelFactory(dataSource, application)

        val createGroupViewModel = ViewModelProvider(this, viewModelFactory).get(CreateGroupViewModel::class.java)

        binding.createGroupViewModel = createGroupViewModel

        binding.lifecycleOwner = this




        binding.createGroupCancelButton.setOnClickListener {
            this.dismiss()
        }


        binding.createGroupCreateButton.setOnClickListener {
//            createGroupViewModel.setGroupName(binding.courseGroupNameInputText.text.toString())
//            createGroupViewModel.setCourseCode(binding.courseCodeInputText.text.toString())
//            createGroupViewModel.setDescription(binding.courseDescriptionInputText.text.toString())

            createGroupViewModel.onCreateGroup(
                groupName = binding.courseGroupNameInputText.text.toString(),
                courseCode = binding.courseCodeInputText.text.toString(),
                desc = binding.courseDescriptionInputText.text.toString()
            )

            this.dismiss()
        }

        createGroupViewModel.showSnackBarEvent.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    "New group created!",
                    Snackbar.LENGTH_LONG
                ).show()
                createGroupViewModel.doneShowingSnackbar()
            }
        })





        return binding.root
    }
}