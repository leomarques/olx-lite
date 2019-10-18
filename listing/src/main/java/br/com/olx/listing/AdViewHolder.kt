package br.com.olx.listing

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import br.com.olx.common.FontProvider
import br.com.olx.common.imageloader.ImageLoader
import br.com.olx.data.local.AdRoom

class AdViewHolder(
    view: View,
    private val context: Context,
    private val imageLoader: ImageLoader,
    private val onListItemClick: (AdRoom) -> Unit
) : RecyclerView.ViewHolder(view) {

    private val title: TextView = view.findViewById(R.id.title)
    private val price: TextView = view.findViewById(R.id.price)
    private val metainfo: TextView = view.findViewById(R.id.metainfo)
    private val thumbnail: ImageView = view.findViewById(R.id.thumbnail)
    private var placeholder: Drawable? = null
    private var errorPlaceholder: Drawable? = null
    private var arrowDown: ImageView = view.findViewById(R.id.arrow_down)
    private var featured: ConstraintLayout = view.findViewById(R.id.featured)
    private var rootLayout: CardView = view.findViewById(R.id.root_layout)

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            placeholder = context.getDrawable(R.drawable.pattern_listing_bg)
            errorPlaceholder = context.getDrawable(R.drawable.pattern_listing_new_bg)
        }
    }

    @SuppressLint("SetTextI18n")
    fun bind(ad: AdRoom?) {
        if (ad != null) {
            title.text = ad.title
            title.typeface = FontProvider.getNunitoSansRegularTypeFace(context)

            price.text = ad.price
            price.typeface = FontProvider.getNunitoSansBoldTypeFace(context)

            arrowDown.visibility =
                if (shouldShowArrowDown(ad.price, ad.oldPrice)) View.VISIBLE else View.GONE

            val formattedDate = formatDate(ad.date)

            metainfo.text = formattedDate + if (ad.location.isNotEmpty()) ", ${ad.location}" else ""
            metainfo.typeface = FontProvider.getNunitoSansRegularTypeFace(context)

            if (ad.thumbUrl.isNotEmpty()) {
                imageLoader.loadImage(
                    context,
                    ad.thumbUrl,
                    thumbnail,
                    placeholder,
                    errorPlaceholder
                )
                if (ad.isFeatured)
                    featured.visibility = View.VISIBLE
                else
                    featured.visibility = View.GONE
            } else
                featured.visibility = View.GONE

            rootLayout.setOnClickListener { onListItemClick(ad) }
        }
    }

    private fun shouldShowArrowDown(price: String, oldPrice: String) =
        oldPrice.isNotEmpty() && price.isNotEmpty() && price < oldPrice

    companion object {
        fun create(
            parent: ViewGroup,
            imageLoader: ImageLoader,
            onListItemClick: (AdRoom) -> Unit
        ): AdViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.ad_view_item, parent, false)
            return AdViewHolder(view, parent.context, imageLoader, onListItemClick)
        }
    }
}
