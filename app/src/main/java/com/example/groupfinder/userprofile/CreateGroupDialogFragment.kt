package com.example.groupfinder.userprofile

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.groupfinder.R
import com.example.groupfinder.calendar.datetimepicker.ReservationViewModelShared
import com.example.groupfinder.database.GroupFinderDatabase
import com.example.groupfinder.databinding.CreateGroupDialogFragmentBinding
import com.example.groupfinder.network.models.Group
import com.example.groupfinder.network.models.PostGroup
import com.google.android.material.snackbar.Snackbar

class CreateGroupDialogFragment : DialogFragment() {

    private val viewModel: UserProfileViewModel by activityViewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle? ): View? {

        val binding = CreateGroupDialogFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel





        binding.groupCreateButton.setOnClickListener {

            val groupName = binding.courseGroupNameInputText.text.toString()
            val description = binding.courseDescriptionInputText.text.toString()
            val courseCode = binding.courseCodeInputText.text.toString()
            val location = binding.locationInputText.text.toString()

            if (groupName.isEmpty()){
                Toast.makeText(context, "Missing group name!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (courseCode.isEmpty()) {
                Toast.makeText(context, "Missing course code!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (location.isEmpty()) {
                Toast.makeText(context, "Missing location!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (description.isEmpty()) {
                Toast.makeText(context, "Missing description!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val group = viewModel.sId.value?.let { it1 ->
                PostGroup(
                    studentId = it1,
                    groupName = groupName,
                    description = description,
                    courseCode = courseCode,
                    location = location
                )
            }


            if (group != null) {
                viewModel.onCreateGroup(group)
                this.dismiss()
            }
        }



        binding.createGroupCancelButton.setOnClickListener {
            this.dismiss()
        }

//        viewModel.showSnackBarEvent.observe(viewLifecycleOwner, Observer {
//            if (it == true) {
//                Snackbar.make(
//                    requireActivity().findViewById(android.R.id.content),
//                    snackText,
//                    Snackbar.LENGTH_LONG
//                ).show()
//                viewModel.doneShowingSnackbar()
//            }
//        })


        return binding.root
    }
}