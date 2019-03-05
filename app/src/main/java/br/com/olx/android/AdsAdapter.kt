package br.com.olx.android

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class AdsAdapter : PagedListAdapter<Ad, RecyclerView.ViewHolder>(AD_COMPARATOR) {

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
        private val AD_COMPARATOR = object : DiffUtil.ItemCallback<Ad>() {
            override fun areItemsTheSame(oldItem: Ad, newItem: Ad): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Ad, newItem: Ad): Boolean =
                oldItem == newItem
        }
    }
}