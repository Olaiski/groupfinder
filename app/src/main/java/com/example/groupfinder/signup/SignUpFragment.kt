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
import com.example.groupfinder.userprofile.UserProfileViewModel
import kotlinx.android.synthetic.main.sign_up_fragment.*

/**
 * [SignUpFragment] inneholder TextField input for å registrere brukere.
 * Bygget opp med [Fragment] og data-binding.
 *
 * @author Anders Olai Pedersen - 225280
 */

class SignUpFragment : Fragment() {


    // Threadsafe
    private val viewModel: SignUpViewModel by lazy {
        ViewModelProvider(this).get(SignUpViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle? ): View? {


        val binding: SignUpFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.sign_up_fragment, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        


        // Register knapp, en del input sjekker
        binding.registerButton.setOnClickListener {

            val forename: String
            val lastname: String
            val email: String
            val phone: Int
            val password: String

            if(binding.forenameInputText.text?.isEmpty()!!) {
                Toast.makeText(context, getString(R.string.missing_info_fn), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else forename = binding.forenameInputText.text.toString()

            if (binding.lastnameInputText.text?.isEmpty()!!) {
                Toast.makeText(context, getString(R.string.missing_info_sn), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else lastname = lastnameInputText.text.toString()

            if (binding.emailInputText.text?.isEmpty()!!) {
                Toast.makeText(context, getString(R.string.missing_info_email), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else email = emailInputText.text.toString()

            if (binding.phoneInputText.text?.isEmpty()!!) {
                Toast.makeText(context, getString(R.string.missing_info_phone), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else phone = phoneInputText.text.toString().toInt()

            if (binding.passwordInputText.text?.isEmpty()!!) {
                Toast.makeText(context, getString(R.string.missing_info_pw), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }else password = passwordInputText.text.toString()
            

            viewModel.onCreateStudent(forename, lastname, email, phone, password)

        }

        // Navigerer til login
        viewModel.navigateToLogin.observe(viewLifecycleOwner, Observer {
            if ( null != it ){
                this.findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToLoginFragment())
                Toast.makeText(context, viewModel.message.value , Toast.LENGTH_LONG).show()
                viewModel.navigateToLoginComplete()
            }
        })


        // Toast med tilbakemelding
        viewModel.message.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                Toast.makeText(context, viewModel.message.value, Toast.LENGTH_LONG).show()
            }
        })


        // Navigere til login via knapp (Hvis man har bruker kan man velge denne), uten backstack.
        val loginFragment = LoginFragment()
        binding.registerToLoginButton.setOnClickListener{
//            this.findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToLoginFragment())
            this.parentFragmentManager.beginTransaction().replace(this.id, loginFragment).addToBackStack(null).commit()
        }


        return binding.root
    }

}