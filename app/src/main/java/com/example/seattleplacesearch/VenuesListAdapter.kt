package com.example.seattleplacesearch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.search_typeahead_card.view.*

class VenuesListAdapter(private val listener: (VenueViewState) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<VenueViewState>() {

        override fun areItemsTheSame(oldItem: VenueViewState, newItem: VenueViewState): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: VenueViewState, newItem: VenueViewState): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return VenueViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.search_typeahead_card,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is VenueViewHolder -> {
                holder.bind(differ.currentList.get(position))
                holder.itemView.setOnClickListener { listener(differ.currentList.get(position)) }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<VenueViewState>) {
        differ.submitList(list)
    }

    class VenueViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: VenueViewState) = with(itemView) {
            venueName.text = item.name
            venueCategory.text = item.category
            venueDistance.text = "${item.distanceToCenter} m"
            Glide.with(venueIcon)
                .load(item.iconUrl.toUri())
                .centerCrop()
                .placeholder(R.drawable.ic_venue_icon_placeholder)
                .into(venueIcon)
        }
    }
}