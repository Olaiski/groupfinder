package com.example.groupfinder.userprofile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.groupfinder.databinding.CardViewItemBinding
import com.example.groupfinder.network.models.Group

class GroupListAdapter (val onClickListener: OnClickListener) :
    ListAdapter<Group, GroupListAdapter.GroupViewHolder>(DiffCallBack){


    class GroupViewHolder(private var binding: CardViewItemBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(group: Group) {
            binding.group = group

            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        return GroupViewHolder(CardViewItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val group = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(group)
        }
        holder.bind(group)
    }

    class OnClickListener(val clickListener: (group: Group) -> Unit) {
        fun onClick(group: Group) = clickListener(group)
    }


    companion object DiffCallBack : DiffUtil.ItemCallback<Group>() {
        override fun areItemsTheSame(oldItem: Group, newItem: Group): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Group, newItem: Group): Boolean {
            return oldItem.gId == newItem.gId
        }
    }

}
