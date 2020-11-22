package com.example.groupfinder.group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.groupfinder.R
import com.example.groupfinder.databinding.GroupFinderFragmentBinding
import com.example.groupfinder.databinding.GroupFragmentBinding
import com.example.groupfinder.network.models.Group

class GroupFinderFragment : Fragment() {

    private val viewModel : GroupFinderViewModel by lazy {
        ViewModelProvider(this).get(GroupFinderViewModel::class.java)
    }




    var groups = ArrayList<Group>()
    var groupAdapter: GroupFinderListAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        val binding: GroupFinderFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.group_finder_fragment, container, false
        )


        binding.lifecycleOwner = this
        binding.vm = viewModel


        viewModel.getGroups()

        viewModel.groups.observe(viewLifecycleOwner, { it1 ->
            groupAdapter = GroupFinderListAdapter(GroupFinderListAdapter.OnClickListener {
                viewModel.displayGroupDetails(it)
            })
            groupAdapter!!.setData(it1 as ArrayList<Group>)
            binding.groupFinderList.adapter = groupAdapter

        })



        viewModel.navigateToSelectedGroup.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                this.findNavController().navigate(GroupFinderFragmentDirections.actionGroupFinderFragmentToGroupFragment(it))
                viewModel.displayGroupDetailsComplete()
            }
        })



//        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                groupAdapterTest!!.filter.filter(query)
//                return true
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                groupAdapterTest!!.filter.filter(newText)
//                return true
//            }
//        })




        return binding.root
    }

}