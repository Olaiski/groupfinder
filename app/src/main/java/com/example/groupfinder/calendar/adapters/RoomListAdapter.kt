package com.example.groupfinder.calendar.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.groupfinder.databinding.RoomItemForRecViewBinding
import com.example.groupfinder.network.models.GroupLeaderGroup
import com.example.groupfinder.network.models.Room

/**
 * Denne klassen implementerer en [RecyclerView] [ListAdapter] som bruker Data Binding til å presentere en [List]
 * data, inkludert beregning av varaisjoner mellom lister.
 * @param onClickListener en lambda som tar gruppen som parameter
 *
 * clickListener får ut informasjoenen om en valgt rom når man reserverer et rom,
 * data blir sendt videre til viewModel.
 *
 * @author Anders Olai Peders - 225280
 */

class RoomListAdapter(val onClickListener: OnClickListener)
    : ListAdapter<Room, RoomListAdapter.RoomViewHolder>(DiffCallBack) {


    /**
     * [RoomViewHolder] -konstruktøren tar bindingsvariabelen fra den tilknyttede
     * GroupLeaderTextView, som gir tilgang til [Room] -informasjonen.
     */
    class RoomViewHolder(private var binding: RoomItemForRecViewBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(room: Room) {
            binding.room = room

            // Viktig, tvinger databindingen til å kjøre umiddelbart,
            // som lar RecyclerView kalkulerer de rette størrelsene
            binding.executePendingBindings()
        }
    }

    /**
     * Oppretter nye [RecyclerView] elemeter. (Invoked av layout manager)
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        return RoomViewHolder(RoomItemForRecViewBinding.inflate(LayoutInflater.from(parent.context)))
    }


    /**
     * Erstatter innholdet i en visning (Invoked av layout manager)
     */
    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val room = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(room)
        }
        holder.bind(room = room)
    }


    /**
     * Tilpasset lytter som håndterer klikk på [RecyclerView] -elementer. Består [Room]
     * tilknyttet gjeldende element til [onClick] -funksjonen.
     * @param clickListener lambda som vil kalle med gjeldende [GroupLeaderGroup]
     */
    class OnClickListener(val clickListener: (room: Room) -> Unit) {
        fun onClick(room: Room) = clickListener(room)
    }


    /**
     * Lar RecycleView bestemme hvilke elementer som har endret seg når [List] av [Room]
     * har blitt oppdatert.
     */
    companion object DiffCallBack : DiffUtil.ItemCallback<Room>() {
        override fun areItemsTheSame(oldItem: Room, newItem: Room): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Room, newItem: Room): Boolean {
            return oldItem.id == newItem.id
        }
    }

}