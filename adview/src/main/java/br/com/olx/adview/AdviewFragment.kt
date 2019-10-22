package br.com.olx.adview

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.com.olx.common.AdviewNavigationModel
import br.com.olx.common.FontProvider
import br.com.olx.common.formatDate
import br.com.olx.common.imageloader.GlideImageLoader
import kotlinx.android.synthetic.main.adview_fragment.*

class AdviewFragment : Fragment() {

    private val imageLoader = GlideImageLoader()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.adview_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val ad = getAd()
        ad ?: return

        val url = if (ad.images.isNotEmpty()) ad.images[0] else ""
        if (url.isNotEmpty())
            imageLoader.loadImage(context!!, url, image, null, null)

        price.text = ad.price
        price.typeface = FontProvider.getNunitoSansRegularTypeFace(context!!)

        old_price.text = ad.oldPrice
        old_price.typeface = FontProvider.getNunitoSansRegularTypeFace(context!!)
        old_price.paintFlags = old_price.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

        title.text = ad.title
        title.typeface = FontProvider.getNunitoSansRegularTypeFace(context!!)

        published_date.text = getString(R.string.adview_publish_date, formatDate(ad.origListTime))
        published_date.typeface = FontProvider.getNunitoSansRegularTypeFace(context!!)

        description_title.typeface = FontProvider.getNunitoSansBoldTypeFace(context!!)

        description.text = ad.description
        description.typeface = FontProvider.getNunitoSansRegularTypeFace(context!!)

        seller_name.text = getString(R.string.adview_seller, ad.sellerName)
        seller_name.typeface = FontProvider.getNunitoSansRegularTypeFace(context!!)
    }

    private fun getAd(): AdviewModel? {
        val ad = arguments?.get("ad") as? AdviewNavigationModel ?: return null

        return convertToAdviewModel(ad)
    }

}