package com.example.groupfinder.calendar.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.groupfinder.databinding.GroupLeaderTextViewBinding
import com.example.groupfinder.network.models.GroupLeaderGroup


/**
 * Denne klassen implementerer en [RecyclerView] [ListAdapter] som bruker Data Binding til å presentere en [List] av
 * data, inkludert beregning av varaisjoner mellom lister.
 * @param onClickListener en lambda som tar gruppen som parameter
 *
 * clickListener får ut informasjoenen om en valgt gruppe når man reserverer rom,
 * data blir sendt videre til viewModel.
 *
 * @author Anders Olai Pedersen - 225280
 */
class GroupLeaderListAdapter(val onClickListener: OnClickListener) :
    ListAdapter<GroupLeaderGroup, GroupLeaderListAdapter.GroupViewHolder>(DiffCallBack) {

    /**
    * [GroupViewHolder] -konstruktøren tar bindingsvariabelen fra den tilknyttede
    * GroupLeaderTextView, som gir tilgang til [GroupLeaderGroup] -informasjonen.
    */
    class GroupViewHolder(private val binding: GroupLeaderTextViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(group: GroupLeaderGroup) {
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
        return GroupViewHolder(GroupLeaderTextViewBinding.inflate(LayoutInflater.from(parent.context)))
    }

    /**
    * Erstatter innholdet i en visning (Invoked av layout manager)
    */
    override fun onBindViewHolder(holderGroup: GroupViewHolder, position: Int) {
        val group = getItem(position)
        holderGroup.itemView.setOnClickListener {
            onClickListener.onClick(group)
        }
        holderGroup.bind(group)
    }

    /**
    * Tilpasset lytter som håndterer klikk på [RecyclerView] -elementer. Består [GroupLeaderGroup]
    * tilknyttet gjeldende element til [onClick] -funksjonen.
    * @param clickListener lambda som vil kalle med gjeldende [GroupLeaderGroup]
    */
    class OnClickListener(val clickListener: (group: GroupLeaderGroup) -> Unit) {
        fun onClick(group: GroupLeaderGroup) = clickListener(group)
    }


    /**
    * Lar RecycleView bestemme hvilke elementer som har endret seg når [List] av [GroupLeaderGroup]
    * har blitt oppdatert.
    */
    companion object DiffCallBack : DiffUtil.ItemCallback<GroupLeaderGroup>() {
        override fun areItemsTheSame(oldItem: GroupLeaderGroup, newItem: GroupLeaderGroup): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: GroupLeaderGroup, newItem: GroupLeaderGroup): Boolean {
            return oldItem.id == newItem.id
        }
    }

}