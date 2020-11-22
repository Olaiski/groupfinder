package com.example.groupfinder.group

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.groupfinder.R
import com.example.groupfinder.network.models.Group
import com.google.android.material.textview.MaterialTextView
import kotlinx.android.synthetic.main.card_view_item.view.*

class GroupFinderListAdapter(val onClickListener: OnClickListener) :
        RecyclerView.Adapter<GroupFinderListAdapter.GroupFinderAdapter>() {

    private var groupList = ArrayList<Group>()
    private var groupListFilter = ArrayList<Group>()

    fun setData(groupList: ArrayList<Group>) {
        this.groupList = groupList
        this.groupListFilter = groupList
        notifyDataSetChanged()
    }


    class GroupFinderAdapter(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val courseCode: MaterialTextView = itemView.group_title_text
        val groupName: MaterialTextView = itemView.group_group_name_text
        val description: MaterialTextView = itemView.group_description_text
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupFinderAdapter {
        return GroupFinderAdapter(LayoutInflater.from(parent.context).inflate(R.layout.card_view_item, parent, false))
    }


    override fun onBindViewHolder(holder: GroupFinderAdapter, position: Int) {
        val group = groupList[position]
        holder.courseCode.text = group.courseCode
        holder.groupName.text = group.groupName
        holder.description.text = group.description

        holder.itemView.setOnClickListener {
            onClickListener.onClick(group)
        }
    }

    /**
     * Tilpasset lytter som håndterer klikk på [RecyclerView] -elementer. Består [Group]
     * tilknyttet gjeldende element til [onClick] -funksjonen.
     * @param clickListener lambda som vil kalle med gjeldende [Group]
     */
    class OnClickListener(val clickListener: (group: Group) -> Unit) {
        fun onClick(group: Group) = clickListener(group)
    }


    override fun getItemCount(): Int {
        return groupList.size
    }
}
