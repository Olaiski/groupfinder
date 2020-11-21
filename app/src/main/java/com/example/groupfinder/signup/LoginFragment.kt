package com.example.groupfinder.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.groupfinder.R
import com.example.groupfinder.databinding.LoginFragmentBinding

import com.example.groupfinder.userprofile.UserProfileViewModel
import com.example.groupfinder.util.Constants
import com.example.groupfinder.util.PreferenceProvider
import kotlin.math.absoluteValue

/**
 * [LoginFragment] inneholder TextField input for login.
 * Bygget opp med [Fragment] og data-binding. Benytter seg av en delt viewModel [UserProfileViewModel]
 *
 * @author Anders Olai Pedersen - 225280
 */
class LoginFragment : Fragment() {


    private val viewModel: UserProfileViewModel by activityViewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle? ): View? {

        // Få en referanse til det bindende objektet og inflate'er fragmentvisningene.
        val binding: LoginFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.login_fragment, container, false)

        // Shared preference
        val pref = this.context?.let { PreferenceProvider(it) }


        binding.viewModel = viewModel
        binding.lifecycleOwner = this



        // Login knapp, henter epost og passord fra tekstfelt
        binding.loginButtonLogin.setOnClickListener {
            val email = binding.loginInputEmail.text.toString()
            val password = binding.loginInputPassword.text.toString()

            if(email.isEmpty()) {
                Toast.makeText(context, "Missing email!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                Toast.makeText(context, "Missing password!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Setter SharedPreference eposten, brukes gjennom hele applikasjonene for queries
            pref?.putEmailPreference(Constants.KEY_EMAIL, email)
            // Henter data om studenten basert på epost (og passord)
            viewModel.onLogin(email, password)
            viewModel.getStudent(email)
            viewModel.getGroups(email)
        }

//        pref?.putIdPreference(Constants.KEY_ID, )

        // Navigerer til profilen om brukeren logger inn
        viewModel.loginSuccess.observe(viewLifecycleOwner, Observer {
            if (it) {
                this.findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToUserProfileFragment())
            }
        })

        viewModel.sId.observe(viewLifecycleOwner, {
            if (it > 0)
                // Setter bruker ID
                pref?.putIdPreference(Constants.KEY_ID, it)
        })


        // Knapp som sender deg til registrering
        val signUpFragment = SignUpFragment()
        binding.loginToRegisterButton.setOnClickListener{
//            this.findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment())
            this.parentFragmentManager.beginTransaction().replace(this.id, signUpFragment).addToBackStack(null).commit()
        }



        return binding.root
    }

}

