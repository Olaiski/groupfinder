package com.example.groupfinder.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.groupfinder.R
import com.example.groupfinder.database.GroupFinderDatabase
import com.example.groupfinder.databinding.SignUpFragmentBinding

class SignUpFragment : Fragment() {

//    companion object {
//        fun newInstance() = SignUpFragment()
//    }
//
//    private lateinit var viewModel: SignUpViewModel
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return inflater.inflate(R.layout.sign_up_fragment, container, false)
//    }
//
//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this).get(SignUpViewModel::class.java)
//        // TODO: Use the ViewModel
//    }


    /**
     * Called when the Fragment is ready to display content to the screen.
     *
     * This function uses DataBindingUtil to inflate R.layout.sign_up_fragment.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle? ): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding: SignUpFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.sign_up_fragment, container, false)

        val application = requireNotNull(this.activity).application

        val dataSource = GroupFinderDatabase.getInstance(application).groupFinderDatabaseDao

        val viewModelFactory = SignUpViewModelFactory(dataSource, application)

        val signUpViewModel =
            ViewModelProvider(this, viewModelFactory).get(SignUpViewModel::class.java)


        binding.signUpViewModel = signUpViewModel

        binding.lifecycleOwner = this






        return binding.root
    }
}