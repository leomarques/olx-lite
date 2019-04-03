package br.com.olx.listing

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.olx.data.local.AdRoom

class AdViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val textView: TextView = view.findViewById(R.id.price)

    fun bind(ad: AdRoom?) {
        if (ad == null) {
            textView.text = "ad == null"
        } else {
            textView.text = "R$" + ad.id
        }
    }

    companion object {
        fun create(parent: ViewGroup): AdViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.ad_view_item, parent, false)
            return AdViewHolder(view)
        }
    }
}
