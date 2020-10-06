package com.example.groupfinder.userprofile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.groupfinder.R
import com.example.groupfinder.databinding.CardViewItemBinding
import com.example.groupfinder.network.models.Group
import com.example.groupfinder.network.models.UserGroups
import kotlinx.android.synthetic.main.card_view_item.view.*

class GroupListAdapter (val event : MutableLiveData<GroupListEvent> = MutableLiveData()) :
    ListAdapter<Group, GroupListAdapter.GroupViewHolder>(DiffCallBack){


    class GroupViewHolder(root: View):
        RecyclerView.ViewHolder(root) {

        var groupName: TextView = root.group_group_name_text
        var description: TextView = root.group_description_text
        var groupTitle: TextView = root.group_title_text
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return GroupViewHolder(
            inflater.inflate(R.layout.card_view_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        getItem(position).let { group ->
            holder.groupName.text = group.groupName
            holder.description.text = group.description
            holder.groupTitle.text = group.courseCode

            holder.itemView.setOnClickListener {
                event.value = GroupListEvent.OnGroupItemClick(position)
            }
        }
    }

    class OnClickListener(val clickListener: (group: Group) -> Unit) {
        fun onClick(group: Group) = clickListener(group)
    }


    companion object DiffCallBack : DiffUtil.ItemCallback<Group>() {
        override fun areItemsTheSame(oldItem: Group, newItem: Group): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Group, newItem: Group): Boolean {
            return oldItem.gId == newItem.gId
        }
    }

    sealed class GroupListEvent {
        data class OnGroupItemClick(val position: Int) : GroupListEvent()
        object OnNewGroupClick : GroupListEvent()
        object OnStart : GroupListEvent()
    }
}

//class GroupListAdapter (groups: List<Group>, val onClickListener: OnClickListener) :
//    ListAdapter<Group, GroupListAdapter.GroupViewHolder>(DiffCallBack){
//
//
//    class GroupViewHolder(private var binding: CardViewItemBinding):
//        RecyclerView.ViewHolder(binding.root) {
//
//        private val description = binding.groupDescriptionText
//        private val groupName = binding.groupGroupNameText
//        private val title = binding.groupTitleText
//
//        fun bind(group: Group) {
//            binding.group = group
//
//            description.text = group.description
//            groupName.text = group.groupName
//            title.text = group.gId.toString()
//
//            binding.executePendingBindings()
//        }
//    }
//
//
//    companion object DiffCallBack : DiffUtil.ItemCallback<Group>() {
//        override fun areItemsTheSame(oldItem: Group, newItem: Group): Boolean {
//            return oldItem == newItem
//        }
//
//        override fun areContentsTheSame(oldItem: Group, newItem: Group): Boolean {
//            return oldItem.gId == newItem.gId
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
//        return GroupViewHolder(CardViewItemBinding.inflate(LayoutInflater.from(parent.context)))
//    }
//
//    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
//        val group = getItem(position)
//        holder.itemView.setOnClickListener {
//            onClickListener.onClick(group)
//        }
//        holder.bind(group)
//    }
//
//    class OnClickListener(val clickListener: (group: Group) -> Unit) {
//        fun onClick(group: Group) = clickListener(group)
//    }
//
//}