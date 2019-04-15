package br.com.olx.listing

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.olx.android.imageloader.ImageLoader
import br.com.olx.data.local.AdRoom
import java.lang.Exception
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class AdViewHolder(view: View, private val context: Context, private val imageLoader: ImageLoader) : RecyclerView.ViewHolder(view) {

    private val title: TextView = view.findViewById(R.id.title)
    private val price: TextView = view.findViewById(R.id.price)
    private val date: TextView = view.findViewById(R.id.date)
    private val thumbnail: ImageView = view.findViewById(R.id.thumbnail)

    fun bind(ad: AdRoom?) {
        if (ad != null) {
            title.text = ad.title
            price.text = ad.price

            val formattedDate = formatDate(ad.date)
            date.text = formattedDate

            if (ad.thumbUrl.isNotEmpty())
                imageLoader.loadImage(context, ad.thumbUrl, thumbnail)
        }
    }

    private fun formatDate(date: String): String {
        return try {
            val dateLong = date.toLong()
            val timeStamp = Timestamp(dateLong)
            val stampTime = timeStamp.time
            val stampDate = Date(stampTime)
            val locale = Locale("pt", "BR")
            val sdf = SimpleDateFormat("dd 'de' MMMM',' HH:mm", locale)
            sdf.format(stampDate)
        } catch (e: Exception) {
            ""
        }
    }

    companion object {
        fun create(parent: ViewGroup, imageLoader: ImageLoader): AdViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.ad_view_item, parent, false)
            return AdViewHolder(view, parent.context, imageLoader)
        }
    }
}
