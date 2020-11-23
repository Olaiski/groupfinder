package com.example.groupfinder.group

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.ShareActionProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.groupfinder.R
import com.example.groupfinder.databinding.GroupFinderFragmentBinding
import com.example.groupfinder.network.models.Group
import com.example.groupfinder.util.Constants
import com.example.groupfinder.util.PreferenceProvider

/**
 * Dette [Fragment] henter og viser alle gruppene, og lar brukeren søke gjennom gruppene (via filter metode i adapteret for RecyclerView'et)
 * Søk baserer seg på kurskode, lokasjon, gruppenavn og beskrivelsen.
 *
 *
 * @author Anders Olai Pedersen - 225280
 */
class GroupFinderFragment : Fragment() {

    private val viewModel : GroupFinderViewModel by lazy {
        ViewModelProvider(this).get(GroupFinderViewModel::class.java)
    }


    var groupAdapter: GroupFinderListAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val pref = this.context?.let { PreferenceProvider(it) }
        pref?.putShowButton(Constants.BTN_SHOW, true)

        val binding: GroupFinderFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.group_finder_fragment, container, false
        )


        binding.lifecycleOwner = this
        binding.vm = viewModel

        // Henter alle gruppene
        viewModel.getGroups()


        // Setter opp adapter
        groupAdapter = GroupFinderListAdapter(GroupFinderListAdapter.OnClickListener {})
        binding.groupFinderList.adapter = groupAdapter


        // Observerer gruppene, setter data og nytt adapter
        viewModel.groups.observe(viewLifecycleOwner, { it1 ->
            groupAdapter = GroupFinderListAdapter(GroupFinderListAdapter.OnClickListener {
                viewModel.displayGroupDetails(it)
            })
            binding.groupFinderList.adapter = groupAdapter
            groupAdapter!!.setData(it1 as ArrayList<Group>)
        })


        // Navigerer til valgt gruppe
        viewModel.navigateToSelectedGroup.observe(viewLifecycleOwner, {
            if (null != it) {
                this.findNavController().navigate(GroupFinderFragmentDirections.actionGroupFinderFragmentToGroupFragment(it))
                viewModel.displayGroupDetailsComplete()
            }
        })


        // Setter opp søk / filter
        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                groupAdapter!!.filter.filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                groupAdapter!!.filter.filter(newText)
                return true
            }
        })



        return binding.root
    }

}