package com.example.groupfinder.userprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.groupfinder.databinding.CreateGroupDialogFragmentBinding
import com.example.groupfinder.network.models.PostGroup
import com.example.groupfinder.util.Constants
import com.example.groupfinder.util.PreferenceProvider


/**
 * [CreateGroupDialogFragment] er bygget på et [DialogFragment].
 * Deler viewmodel [UserProfileFragment]
 * Input for opprettelse av grupper. post request til API'et.
 *
 * @author Anders Olai Pedersen - 225280
 */
class CreateGroupDialogFragment : DialogFragment() {

    private val viewModel: UserProfileViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle? ): View? {

        val pref = this.context?.let { PreferenceProvider(it) }
        val studentId = pref?.getIdPreference(Constants.KEY_ID)

        val binding = CreateGroupDialogFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel


        // Bygger opp gruppen, knapp sender info videre til viewmodel
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


            val group = PostGroup(studentId!!, groupName, description, courseCode, location)

            viewModel.onCreateGroup(group)
            this.dismiss()
        }


        // Knapp for å lukke dialog
        binding.createGroupCancelButton.setOnClickListener {
            this.dismiss()
        }


        return binding.root
    }

}