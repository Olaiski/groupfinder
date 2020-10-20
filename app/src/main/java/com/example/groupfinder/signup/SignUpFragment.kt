package com.example.groupfinder.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.groupfinder.R
import com.example.groupfinder.databinding.SignUpFragmentBinding
import com.example.groupfinder.login.ui.login.LoginFragment
import com.example.groupfinder.network.models.PostStudent
import com.example.groupfinder.userprofile.UserProfileFragmentDirections
import kotlinx.android.synthetic.main.sign_up_fragment.*
import kotlin.math.log

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


        binding.registerButton.setOnClickListener {

            val forename: String
            val lastname: String
            val email: String
            val phone: Int
            val password: String

            // TODO: 15/10/2020 Trenger vi en if's? Bedre løsning?
            if(binding.forenameInputText.text?.isEmpty()!!) {
                Toast.makeText(context, "Missing forename!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else forename = binding.forenameInputText.text.toString()

            if (binding.lastnameInputText.text?.isEmpty()!!) {
                Toast.makeText(context, "Missing surname!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else lastname = lastnameInputText.text.toString()

            if (binding.emailInputText.text?.isEmpty()!!) {
                Toast.makeText(context, "Missing Email!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else email = emailInputText.text.toString()

            if (binding.phoneInputText.text?.isEmpty()!!) {
                Toast.makeText(context, "Missing phone!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else phone = phoneInputText.text.toString().toInt()

            if (binding.passwordInputText.text?.isEmpty()!!) {
                Toast.makeText(context, "Missing password!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }else password = passwordInputText.text.toString()
            

            viewModel.onCreateStudent(forename, lastname, email, phone, password)

        }

        viewModel.navigateToLogin.observe(viewLifecycleOwner, Observer {
            if ( null != it ){
                this.findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToLoginFragment())
                Toast.makeText(context, viewModel.message.value , Toast.LENGTH_LONG).show()
                viewModel.navigateToLoginComplete()
            }
        })


        viewModel.message.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                Toast.makeText(context, viewModel.message.value, Toast.LENGTH_LONG).show()
            }
        })

        val loginFragment = LoginFragment()

        binding.registerToLoginButton.setOnClickListener{
//            this.findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToLoginFragment())
            this.parentFragmentManager.beginTransaction().replace(this.id, loginFragment).addToBackStack(null).commit()
        }


        return binding.root
    }

}