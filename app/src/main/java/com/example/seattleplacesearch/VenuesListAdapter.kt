package com.example.seattleplacesearch

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.typeahead_card.view.*

class VenuesListAdapter(private val listener: (Venue) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Venue>() {

        override fun areItemsTheSame(oldItem: Venue, newItem: Venue): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Venue, newItem: Venue): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return VenueViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.typeahead_card,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is VenueViewHolder -> {
                holder.bind(differ.currentList.get(position))
                holder.itemView.setOnClickListener{listener(differ.currentList.get(position))}
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<Venue>) {
        differ.submitList(list)
    }

    class VenueViewHolder
    constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Venue) = with(itemView) {
            venueName.text = item.name
            venueCategory.text = item.category
            venueDistance.text = MapUrlGenerator.getDistanceToSeattleCenter(
                LatLng(
                    item.location.lat.toDouble(),
                    item.location.lng.toDouble()
                )
            )
            Glide.with(venueIcon)
                .load(item.iconUrl.toUri())
                .centerCrop()
                .into(venueIcon)
        }
    }
}