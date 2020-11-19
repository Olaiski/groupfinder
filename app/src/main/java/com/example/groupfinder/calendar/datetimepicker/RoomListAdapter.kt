package com.example.groupfinder.calendar.datetimepicker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.groupfinder.databinding.RoomItemForRecViewBinding
import com.example.groupfinder.network.models.Room

class RoomListAdapter(val onClickListener: OnClickListener)
    : ListAdapter<Room, RoomListAdapter.RoomViewHolder>(DiffCallBack) {



    class RoomViewHolder(private var binding: RoomItemForRecViewBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(room: Room) {
            binding.room = room

            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        return RoomViewHolder(RoomItemForRecViewBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val room = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(room)
        }
        holder.bind(room = room)
    }

    class OnClickListener(val clickListener: (room: Room) -> Unit) {
        fun onClick(room: Room) = clickListener(room)
    }

    companion object DiffCallBack : DiffUtil.ItemCallback<Room>() {
        override fun areItemsTheSame(oldItem: Room, newItem: Room): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Room, newItem: Room): Boolean {
            return oldItem.id == newItem.id
        }
    }

}