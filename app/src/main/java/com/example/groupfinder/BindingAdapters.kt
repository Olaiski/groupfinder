package com.example.groupfinder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.groupfinder.group.StudentMembersListAdapter
import com.example.groupfinder.network.models.Group
import com.example.groupfinder.network.models.Student
import com.example.groupfinder.userprofile.ApiStatus
import com.example.groupfinder.userprofile.GroupListAdapter
import com.google.android.material.textview.MaterialTextView


/**
 * When there is no Group data (data is null), hide the [RecyclerView], otherwise show it.
 */
@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Group>?) {
    val adapter = recyclerView.adapter as GroupListAdapter
    adapter.submitList(data)
}

@BindingAdapter("studentData")
fun bindRecyclerViewGroupMembers(recyclerView: RecyclerView, data: List<Student>?) {
    val adapter = recyclerView.adapter as StudentMembersListAdapter
    adapter.submitList(data)
}

/**
 *  This binding adapter displays the [ApiStatus] of the network request in an image view.
 *  When the request is loading, displays a loading_animation. If the request has an error it
 *  displays a broken image to reflect the connection error. When the request is finished, it hides the
 *  image view.
 */
@BindingAdapter("apiStatus")
fun bindStatus(statusImageView: ImageView, status: ApiStatus?) {
    when(status) {
        ApiStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }
        ApiStatus.ERROR -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }
        ApiStatus.DONE -> {
            statusImageView.visibility = View.GONE
        }
    }
}


// FIXME: 09/10/2020 Blir ikke brukt per nå.. Se nærmere på senere.  
@BindingAdapter("studentData")
fun bindStudentData(textView: MaterialTextView, data: String) {
    if (data.isEmpty()) {
        textView.text = "..."
    } else {
        textView.text = data
    }
}