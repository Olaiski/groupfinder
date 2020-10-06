package com.example.groupfinder.userprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.groupfinder.databinding.UserProfileFragmentBinding
import com.example.groupfinder.network.GroupFinderApiService
import com.example.groupfinder.network.ServiceBuilder
import com.example.groupfinder.network.models.ResponseStudent
import com.example.groupfinder.network.models.Student
import com.example.groupfinder.network.models.UserGroups
import kotlinx.android.synthetic.main.user_profile_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UserProfileFragment : Fragment() {


//    private val viewModel: UserProfileViewModel by activityViewModels()

    private lateinit var adapter: GroupListAdapter

    private val viewModel: UserProfileViewModel by lazy {
        ViewModelProvider(this).get(UserProfileViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding = UserProfileFragmentBinding.inflate(inflater)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel


        val createGroupDialog = CreateGroupDialogFragment()
        val createGroupButton = binding.createGroupButton




        createGroupButton.setOnClickListener {
            createGroupDialog.show(parentFragmentManager, "cgd")
        }


        val request = ServiceBuilder.buildService(GroupFinderApiService::class.java)


        /**
         * Gets and set user groups based on email (Query / ApiService)
         */
        val groupCall = request.getUserGroups(viewModel.email.value.toString())
        groupCall.enqueue(object : Callback<UserGroups> {
            override fun onResponse(call: Call<UserGroups>, response: Response<UserGroups>) {
                if (response.isSuccessful) {


                    val groupResponse = response.body()!!
//                    println(groupResponse.userGroups)
//                    println(groupResponse.message)

                    viewModel.setGroups(groupResponse.userGroups)

                    setUpAdapter()
                    observeViewModel()
                }
            }

            override fun onFailure(call: Call<UserGroups>, t: Throwable) {
                println("Failure  ${t.message}")
            }

        })

        /**
         * Gets and set student based on email (Query / ApiService)
         */
        val studentCall = request.getStudent(viewModel.email.value.toString())
        studentCall.enqueue(object : Callback<ResponseStudent> {
            override fun onResponse(call: Call<ResponseStudent>, response: Response<ResponseStudent>) {
                if (response.isSuccessful) {
                    val studentResponse = response.body()!!

//                    println(studentResponse)
                    viewModel.setStudent(studentResponse)
                }
            }

            override fun onFailure(call: Call<ResponseStudent>, t: Throwable) {
                println("Failure  ${t.message}")
            }
        })




//        binding.userGroupList.adapter = GroupListAdapter(groups = groupList, GroupListAdapter.OnClickListener {
//            viewModel.displayGroupDetails(it)
//        })



        return binding.root
    }

    private fun setUpAdapter() {
        adapter = GroupListAdapter()
        adapter.event.observe(
            viewLifecycleOwner,
            Observer {
                viewModel.displayGroupDetails(it)
            }
        )
        user_group_list.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.groups.observe(
            viewLifecycleOwner,
            Observer { groupList ->
                adapter.submitList(groupList)

                if (groupList.isNotEmpty()) {

                }

            }
        )
    }



    override fun onDestroyView() {
        super.onDestroyView()
        user_group_list.adapter = null
    }

    private fun showWelcomeMessage() {
        // ...
    }
}