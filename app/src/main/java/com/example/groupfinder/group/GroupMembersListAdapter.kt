package com.example.groupfinder.group

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.groupfinder.databinding.UserCardInfoBinding
import com.example.groupfinder.network.models.Student

class StudentMembersListAdapter :
        ListAdapter<Student, StudentMembersListAdapter.StudentViewHolder>(DiffCallBack){


    class StudentViewHolder(private var binding: UserCardInfoBinding):
            RecyclerView.ViewHolder(binding.root) {
        fun bind(student: Student) {
            binding.student = student

            binding.executePendingBindings()
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        return StudentViewHolder(UserCardInfoBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val studentViewHolder = getItem(position)

    }

    companion object DiffCallBack : DiffUtil.ItemCallback<Student>() {
        override fun areItemsTheSame(oldItem: Student, newItem: Student): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Student, newItem: Student): Boolean {
            return oldItem.id == newItem.id
        }
    }

}