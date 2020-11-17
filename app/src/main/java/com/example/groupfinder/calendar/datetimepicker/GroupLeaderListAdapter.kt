package com.example.groupfinder.calendar.datetimepicker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.groupfinder.databinding.GroupLeaderTextViewBinding
import com.example.groupfinder.network.models.GroupLeaderGroup

class GroupLeaderListAdapter(val onClickListener: OnClickListener) :
    ListAdapter<GroupLeaderGroup, GroupLeaderListAdapter.GroupViewHolder>(DiffCallBack) {


    class GroupViewHolder(private val binding: GroupLeaderTextViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(group: GroupLeaderGroup) {
            binding.group = group

            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        return GroupViewHolder(GroupLeaderTextViewBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holderGroup: GroupViewHolder, position: Int) {
        val group = getItem(position)
        holderGroup.itemView.setOnClickListener {
            onClickListener.onClick(group)
        }
        holderGroup.bind(group)
    }


    class OnClickListener(val clickListener: (group: GroupLeaderGroup) -> Unit) {
        fun onClick(group: GroupLeaderGroup) = clickListener(group)
    }

    companion object DiffCallBack : DiffUtil.ItemCallback<GroupLeaderGroup>() {
        override fun areItemsTheSame(oldItem: GroupLeaderGroup, newItem: GroupLeaderGroup): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: GroupLeaderGroup, newItem: GroupLeaderGroup): Boolean {
            return oldItem.id == newItem.id
        }
    }

}