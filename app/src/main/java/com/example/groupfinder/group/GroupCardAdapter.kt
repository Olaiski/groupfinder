package com.example.groupfinder.group

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.groupfinder.R
import com.example.groupfinder.database.models.Group
import com.example.groupfinder.databinding.CardViewItemBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.ClassCastException


// TODO: 03/09/2020  FIX ME! Adapter til view cards? Recyclerview etc...

private val ITEM_VIEW_TYPE_HEADER = 0
private val ITEM_VIEW_TYPE_ITEM = 1

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
                when (holder) {
                        is ViewHolder -> {
                           val groupItem = getItem(position) as DataItem.GroupItem
                                holder.bind(clickListener, groupItem.group)
                        }
                }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                return when (viewType) {
                        ITEM_VIEW_TYPE_HEADER -> TextViewHolder.from(parent)
                        ITEM_VIEW_TYPE_ITEM -> ViewHolder.from(parent)
                        else -> throw ClassCastException("Unknown viewType ${viewType}")
                }
        }

        class TextViewHolder (view: View): RecyclerView.ViewHolder(view) {
                companion object {
                        fun from(parent: ViewGroup): TextViewHolder {
                                val layoutInflater = LayoutInflater.from(parent.context)
                                val view = layoutInflater.inflate(R.layout.my_groups_header, parent, false)

                                return TextViewHolder(view)
                        }
                }
        }

        override fun getItemViewType(position: Int): Int {
                return when (getItem(position)) {
                        is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
                        is DataItem.GroupItem -> ITEM_VIEW_TYPE_ITEM
                }
        }



}

class ViewHolder private constructor(val binding: CardViewItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind (clickListener: GroupListener, item: Group) {
                binding.group = item
                binding.clickListener = clickListener
                binding.executePendingBindings()
        }

        companion object {
                fun from(parent: ViewGroup): ViewHolder {
                        val layoutInflater = LayoutInflater.from(parent.context)
                        val binding = CardViewItemBinding.inflate(layoutInflater, parent, false)

                        return ViewHolder(binding)
                }
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