package com.example.groupfinder.calendar.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.groupfinder.databinding.ReservationInfoItemBinding
import com.example.groupfinder.network.models.GroupLeaderGroup
import com.example.groupfinder.network.models.UserReservation
/**
 * Denne klassen implementerer en [RecyclerView] [ListAdapter] som bruker Data Binding til å presentere en [List] av
 * data, inkludert beregning av varaisjoner mellom lister.
 * @param onClickListener en lambda som tar gruppen som parameter
 *
 * clickListener får ut informasjoenen om en reservasjon, data blir sendt videre til viewModel.
 *
 * Denne klassen blir egentlig ikke brukt til noe nytting per nå. Men tanker er at når man trykker på
 * en reservasjon, blir man sendt til et nytt view der man kan endre på reservasjonen.
 *
 * @author Anders Olai Pedersen - 225280
 */
class UserReservationListAdapter (private val onClickListener: OnClickListener) :
    ListAdapter<UserReservation, UserReservationListAdapter.ReservationViewHolder>(DiffCallBack) {


    /**
     * [ReservationViewHolder] -konstruktøren tar bindingsvariabelen fra den tilknyttede
     * GroupLeaderTextView, som gir tilgang til [UserReservation] -informasjonen.
     */
    class ReservationViewHolder(private var binding: ReservationInfoItemBinding):
            RecyclerView.ViewHolder(binding.root) {
        fun bind(userReservation: UserReservation) {
            binding.reservation = userReservation

            binding.executePendingBindings()
        }
    }

    /**
     * Oppretter nye [RecyclerView] elemeter. (Invoked av layout manager)
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationViewHolder {
        return ReservationViewHolder(ReservationInfoItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    /**
     * Erstatter innholdet i en visning (Invoked av layout manager)
     */
    override fun onBindViewHolder(holder: ReservationViewHolder, position: Int) {
        val reservation = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(reservation)
        }
        holder.bind(reservation)
    }


    /**
     * Tilpasset lytter som håndterer klikk på [RecyclerView] -elementer. Består [UserReservation]
     * tilknyttet gjeldende element til [onClick] -funksjonen.
     * @param clickListener lambda som vil kalle med gjeldende [UserReservation]
     */
    class OnClickListener(val clickListener: (reservation: UserReservation) -> Unit) {
        fun onClick(reservation: UserReservation) = clickListener(reservation)
    }

    /**
     * Lar RecycleView bestemme hvilke elementer som har endret seg når [List] av [UserReservation]
     * har blitt oppdatert.
     */
    companion object DiffCallBack : DiffUtil.ItemCallback<UserReservation>() {
        override fun areItemsTheSame(oldItem: UserReservation, newItem: UserReservation): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: UserReservation, newItem: UserReservation): Boolean {
            return oldItem.rId == newItem.rId
        }
    }

}