package com.example.groupfinder.group

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.groupfinder.databinding.CardViewItemBinding
import com.example.groupfinder.network.models.Group




//class GroupListAdapter(val onClickListener: OnClickListener) :
//        ListAdapter<Group, GroupListAdapter.GroupViewHolder>(DiffCallBack){
//
//
//        class GroupViewHolder(private var binding: CardViewItemBinding):
//                RecyclerView.ViewHolder(binding.root) {
//                fun bind(group: Group) {
//                        binding.group = group
//                        binding.executePendingBindings()
//                }
//        }
//
//
//        companion object DiffCallBack : DiffUtil.ItemCallback<Group>() {
//                override fun areItemsTheSame(oldItem: Group, newItem: Group): Boolean {
//                        return oldItem == newItem
//                }
//
//                override fun areContentsTheSame(oldItem: Group, newItem: Group): Boolean {
//                        return oldItem.id == newItem.id
//                }
//        }
//
//        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
//                return GroupViewHolder(CardViewItemBinding.inflate(LayoutInflater.from(parent.context)))
//        }
//
//        override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
//                val group = getItem(position)
//                holder.itemView.setOnClickListener {
//                        onClickListener.onClick(group)
//                }
//        }
//
//        class OnClickListener(val clickListener: (group: Group) -> Unit) {
//                fun onClick(group:Group) = clickListener(group)
//        }
//
//}