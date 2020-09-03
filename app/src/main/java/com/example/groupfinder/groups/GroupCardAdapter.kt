package com.example.groupfinder.groups

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.groupfinder.database.models.Group
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class GroupCardAdapter(val clickListener: GroupListener) :
        ListAdapter<DataItem, RecyclerView.ViewHolder>(GroupDiffCallback()) {

        private val adapterScope = CoroutineScope(Dispatchers.Default)


        fun addHeaderAndSubmitList(list: List<Group>?) {
                adapterScope.launch {
                        val items = when (list) {
                                null -> listOf(DataItem.Header)
                                else -> listOf(DataItem.Header) + list.map { DataItem.GroupItem(it) }
                        }
                        withContext(Dispatchers.Main) {
                                submitList(items)
                        }
                }
        }


        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                TODO("Not yet implemented")
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                TODO("Not yet implemented")
        }



}





class GroupDiffCallback : DiffUtil.ItemCallback<DataItem>() {
        override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem == newItem
        }
}


class GroupListener(val clickListener: (groupId: Long) -> Unit) {
        fun onClick(group: Group) = clickListener(group.groupId)
}

sealed class DataItem {
        data class GroupItem(val group: Group): DataItem() {
                override val id = group.groupId
        }

        object Header: DataItem() {
                override val id = Long.MIN_VALUE
        }

        abstract val id: Long
}