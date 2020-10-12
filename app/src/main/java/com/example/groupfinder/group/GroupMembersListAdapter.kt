package com.example.groupfinder.group

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.groupfinder.databinding.UserCardInfoBinding
import com.example.groupfinder.network.models.StudentCompact

class StudentMembersListAdapter (val onClickListener : OnClickListener) :
        ListAdapter<StudentCompact, StudentMembersListAdapter.StudentViewHolder>(DiffCallBack){


    class StudentViewHolder(private var binding: UserCardInfoBinding):
            RecyclerView.ViewHolder(binding.root) {
        fun bind(student: StudentCompact) {
            binding.student = student

            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        return StudentViewHolder(UserCardInfoBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(student)
        }
        holder.bind(student)
    }

    class OnClickListener(val clickListener: (student: StudentCompact) -> Unit) {
        fun onClick(student: StudentCompact) = clickListener(student)
    }

    companion object DiffCallBack : DiffUtil.ItemCallback<StudentCompact>() {
        override fun areItemsTheSame(oldItem: StudentCompact, newItem: StudentCompact): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: StudentCompact, newItem: StudentCompact): Boolean {
            return oldItem.id == newItem.id
        }
    }

}