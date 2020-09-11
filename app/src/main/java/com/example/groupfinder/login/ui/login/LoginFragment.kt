package com.example.groupfinder.login.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.groupfinder.R
import com.example.groupfinder.database.GroupFinderDatabase
import com.example.groupfinder.databinding.LoginFragmentBinding

class LoginFragment : Fragment() {

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

        val application = requireNotNull(this.activity).application

        val dataSource = GroupFinderDatabase.getInstance(application).groupFinderDatabaseDao

        val viewModelFactory = LoginViewModelFactory(dataSource, application)

        val loginViewModel = ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)

        binding.loginViewModel = loginViewModel

        binding.lifecycleOwner = this


        return binding.root
    }




}