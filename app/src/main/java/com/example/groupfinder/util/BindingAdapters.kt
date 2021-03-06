package com.example.groupfinder.util

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.groupfinder.R
import com.example.groupfinder.calendar.adapters.UserReservationListAdapter
import com.example.groupfinder.calendar.adapters.GroupLeaderListAdapter
import com.example.groupfinder.calendar.adapters.RoomListAdapter
import com.example.groupfinder.group.StudentMembersListAdapter
import com.example.groupfinder.userprofile.ApiStatus
import com.example.groupfinder.userprofile.GroupListAdapter
import com.example.groupfinder.group.GroupFragment
import com.example.groupfinder.group.GroupViewModel
import com.example.groupfinder.network.models.*


/**
 * Når det ikke er gruppe- / studentdata (data er null), skjul [RecyclerView], ellers viser det.
 */
@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Group>?) {
    (recyclerView.adapter as? GroupListAdapter)?.submitList(data)
}


@BindingAdapter("studentData")
fun bindRecyclerViewGroupMembers(recyclerView: RecyclerView, data: List<StudentCompact>?) {
    val adapter = recyclerView.adapter as StudentMembersListAdapter
    adapter.submitList(data)
}

@BindingAdapter("groupData")
fun bindRecyclerViewGroupLeaderGroups(recyclerView: RecyclerView, data: List<GroupLeaderGroup>?) {
    val adapter = recyclerView.adapter as GroupLeaderListAdapter
    adapter.submitList(data)
}

@BindingAdapter("roomData")
fun bindRecyclerViewRoomData(recyclerView: RecyclerView, data: List<Room>?) {
    val adapter = recyclerView.adapter as RoomListAdapter
    adapter.submitList(data)
}

@BindingAdapter("bind:userReservationData")
fun bindRecyclerViewUserReservation(recyclerView: RecyclerView, data: List<UserReservation>?) {
    val adapter = recyclerView.adapter as UserReservationListAdapter
    adapter.submitList(data)
}

/**
 * Bindende adapter for å sette synlighet av knapper i [GroupFragment] / [GroupViewModel]
 */
@BindingAdapter("joinButtonVisible")
fun setJoinButtonVisible(view: View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.INVISIBLE
}

@BindingAdapter("cancelButtonVisible")
fun setCancelButtonVisible(view: View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.INVISIBLE
}

/**
 *  Denne bindingsadapteren viser [ApiStatus] til nettverksforespørselen i en bildevisning.
 *  Når forespørselen lastes, viser en loading_animation. Hvis forespørselen har en feil det
 *  viser et ødelagt bilde for å gjenspeile tilkoblingsfeilen. Når forespørselen er ferdig, skjuler den
 *  bildevisning.
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
