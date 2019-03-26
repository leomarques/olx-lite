package br.com.olx.listing

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import br.com.olx.data.AdRoom

class AdsAdapter : PagedListAdapter<AdRoom, RecyclerView.ViewHolder>(AD_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return AdViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val adItem = getItem(position)
        if (adItem != null) {
            (holder as AdViewHolder).bind(adItem)
        }
    }

    companion object {
        private val AD_COMPARATOR = object : DiffUtil.ItemCallback<AdRoom>() {
            override fun areItemsTheSame(oldItem: AdRoom, newItem: AdRoom): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: AdRoom, newItem: AdRoom): Boolean =
                oldItem == newItem
        }
    }
}