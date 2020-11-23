package com.example.groupfinder.group

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.groupfinder.R
import com.example.groupfinder.network.models.Group
import com.example.groupfinder.userprofile.GroupListAdapter.GroupViewHolder
import com.google.android.material.textview.MaterialTextView
import kotlinx.android.synthetic.main.card_view_item.view.*
import java.util.*
import kotlin.collections.ArrayList


/**
 * Denne klassen implementerer en [RecyclerView.Adapter] og [Filterable] for å søke gjennom gruppene,
 * søkene baserer seg på info teksten til gruppen (kurskode, lokasjon, gruppenavn og beskrivelse).
 *
 * Data for listen blir ikke satt via binding men via en setter metode. Dette er pga. vi må søke
 * gjennom listene med filter metodene (kunne sikkert tatt det inn som en param.).
 *
 * @param onClickListener en lambda som tar gruppen som parameter
 *
 * @author Anders Olai Pedersen - 225280
 */
class GroupFinderListAdapter(val onClickListener: OnClickListener) :
        RecyclerView.Adapter<GroupFinderListAdapter.GroupFinderAdapter>(), Filterable {

    private var groupList = ArrayList<Group>()
    private var groupListFilter = ArrayList<Group>()

    fun setData(groupList: ArrayList<Group>) {
        this.groupList = groupList
        this.groupListFilter = groupList
        notifyDataSetChanged()
    }

    /**
     * [GroupFinderAdapter] -konstruktøren tar inn [View] til den tilknyttede
     * gruppen.
     */
    class GroupFinderAdapter(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val courseCode: MaterialTextView = itemView.group_title_text
        val groupName: MaterialTextView = itemView.group_group_name_text
        val description: MaterialTextView = itemView.group_description_text
    }


    /**
     * Oppretter nye [RecyclerView] elemeter. (Invoked av layout manager)
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupFinderAdapter {
        return GroupFinderAdapter(LayoutInflater.from(parent.context).inflate(R.layout.card_view_item, parent, false))
    }

    /**
     * Erstatter innholdet i en visning (Invoked av layout manager)
     */
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


    /**
     * Størrelse på listen
     */
    override fun getItemCount(): Int {
        return groupList.size
    }

    /**
     * Filter for søk.
     */
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val filterResults = FilterResults()

                if (charSequence == null || charSequence.length < 0) {
                    filterResults.count = groupListFilter.size
                    filterResults.values = groupListFilter
                } else {
                    val searchChar = charSequence.toString()

                    val groups = ArrayList<Group>()
                    for(item in groupListFilter) {
                        if (item.courseCode.contains(searchChar, ignoreCase = true) ||
                            item.description.contains(searchChar, ignoreCase = true) ||
                            item.location.contains(searchChar, ignoreCase = true) ||
                            item.groupName.contains(searchChar, ignoreCase = true))

                            groups.add(item)
                    }
                    filterResults.count = groups.size
                    filterResults.values = groups
                }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                groupList = results!!.values as ArrayList<Group>
                notifyDataSetChanged()
            }
        }
    }
}
