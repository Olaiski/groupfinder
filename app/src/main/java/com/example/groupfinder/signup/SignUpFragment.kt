package com.example.groupfinder.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.groupfinder.R
import com.example.groupfinder.database.GroupFinderDatabase
import com.example.groupfinder.databinding.SignUpFragmentBinding
import com.example.groupfinder.network.models.PostStudent
import com.example.groupfinder.network.models.Student

class SignUpFragment : Fragment() {


    private val viewModel: SignUpViewModel by lazy {
        ViewModelProvider(this).get(SignUpViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle? ): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding: SignUpFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.sign_up_fragment, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel


        binding.loginButtonLogin.setOnClickListener {

            val forename = binding.forenameInputText.text.toString()
            val surname = binding.lastnameInputText.text.toString()
            val email = binding.emailInputText.text.toString()
            val phone = binding.phoneInputText.text.toString().toInt()
            val password = binding.passwordInputText.text.toString()

            // TODO: 15/10/2020 Trenger vi en if's? Bedre løsning?
            if(forename.isEmpty()) {
                Toast.makeText(context, "Missing forename!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (surname.isEmpty()) {
                Toast.makeText(context, "Missing surname!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (email.isEmpty()) {
                Toast.makeText(context, "Missing Email!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (phone.toString().isEmpty()) {
                Toast.makeText(context, "Missing phone!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                Toast.makeText(context, "Missing password!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            val student = PostStudent(forename,surname,email,phone,password)

            viewModel.onCreateStudent(student)


        }



        return binding.root
    }

}