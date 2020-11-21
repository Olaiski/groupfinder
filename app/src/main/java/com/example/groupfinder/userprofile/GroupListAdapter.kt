package com.example.groupfinder.userprofile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.groupfinder.databinding.CardViewItemBinding
import com.example.groupfinder.network.models.Group


/**
 * Denne klassen implementerer en [RecyclerView] [ListAdapter] som bruker Data Binding til å presentere [List]
 * data, inkludert beregning av varaisjoner mellom lister.
 * @param onClickListener en lambda som tar gruppen som parameter
 *
 * @author Anders Olai Peders - 225280
 */
class GroupListAdapter (val onClickListener: OnClickListener) :
    ListAdapter<Group, GroupListAdapter.GroupViewHolder>(DiffCallBack){


    /**
     * The [GroupViewHolder] constructor takes the binding variable from the associated
     * CardViewItem, which gives access to the [Group] information.
     */
    class GroupViewHolder(private var binding: CardViewItemBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(group: Group) {
            binding.group = group

            // Important, forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
            binding.executePendingBindings()
        }
    }

    /**
     * Allows the RecycleView to determine which items have changed when the [List] of [Group]
     * has been updated.
     */
    companion object DiffCallBack : DiffUtil.ItemCallback<Group>() {
        override fun areItemsTheSame(oldItem: Group, newItem: Group): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Group, newItem: Group): Boolean {
            return oldItem.gId == newItem.gId
        }
    }

    /**
     * Create new [RecyclerView] item views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        return GroupViewHolder(CardViewItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val group = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(group)
        }
        holder.bind(group)
    }


    /**
     * Custom listener that handles clicks on [RecyclerView] items. Passes the [Group]
     * associated with the current item to the [onClick] function.
     * @param clickListener lambda that will be called with the current [Group]
     */
    class OnClickListener(val clickListener: (group: Group) -> Unit) {
        fun onClick(group: Group) = clickListener(group)
    }

}
