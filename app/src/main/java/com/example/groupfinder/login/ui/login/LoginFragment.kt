package com.example.groupfinder.login.ui.login

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
import com.example.groupfinder.signup.SignUpFragment
import com.example.groupfinder.userprofile.UserProfileViewModel

class LoginFragment : Fragment() {

//    private val viewModel: LoginViewModel by lazy {
//        ViewModelProvider(this).get(LoginViewModel::class.java)
//    }

    private val viewModel: UserProfileViewModel by activityViewModels()

    /**
     * Called when the Fragment is ready to display content to the screen.
     *
     * This function uses DataBindingUtil to inflate R.layout.login_fragment.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle? ): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding: LoginFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.login_fragment, container, false)


        binding.viewModel = viewModel
        binding.lifecycleOwner = this



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

            viewModel.onLogin(email, password)
            viewModel.getStudent(email)
            viewModel.getGroups(email)

        }


        viewModel.loginSuccess.observe(viewLifecycleOwner, Observer {
            if (it)
                this.findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToUserProfileFragment())
        })



        val signUpFragment = SignUpFragment()

        binding.loginToRegisterButton.setOnClickListener{
//            this.findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment())
            this.parentFragmentManager.beginTransaction().replace(this.id, signUpFragment).addToBackStack(null).commit()
        }





        return binding.root
    }

}

