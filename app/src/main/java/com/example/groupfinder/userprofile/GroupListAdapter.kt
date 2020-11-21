package com.example.groupfinder.userprofile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.groupfinder.databinding.CardViewItemBinding
import com.example.groupfinder.network.models.Group
import com.example.groupfinder.network.models.GroupLeaderGroup


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
     * [GroupViewHolder] -konstruktøren tar bindingsvariabelen fra den tilknyttede
     * GroupLeaderTextView, som gir tilgang til [Group] -informasjonen.
     */
    class GroupViewHolder(private var binding: CardViewItemBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(group: Group) {
            binding.group = group

            // Viktig, tvinger databindingen til å kjøre umiddelbart,
            // som lar RecyclerView kalkulerer de rette størrelsene
            binding.executePendingBindings()
        }
    }

    /**
     * Oppretter nye [RecyclerView] elemeter. (Invoked av layout manager)
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        return GroupViewHolder(CardViewItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    /**
     * Erstatter innholdet i en visning (Invoked av layout manager)
     */
    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val group = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(group)
        }
        holder.bind(group)
    }

    /**
     * Tilpasset lytter som håndterer klikk på [RecyclerView] -elementer. Består [Group]
     * tilknyttet gjeldende element til [onClick] -funksjonen.
     * @param clickListener lambda som vil kalle med gjeldende [Group]
     */
    class OnClickListener(val clickListener: (group: Group) -> Unit) {
        fun onClick(group: Group) = clickListener(group)
    }

    /**
     * Lar RecycleView bestemme hvilke elementer som har endret seg når [List] av [GroupLeaderGroup]
     * har blitt oppdatert.
     */
    companion object DiffCallBack : DiffUtil.ItemCallback<Group>() {
        override fun areItemsTheSame(oldItem: Group, newItem: Group): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Group, newItem: Group): Boolean {
            return oldItem.gId == newItem.gId
        }
    }

}
