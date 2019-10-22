package br.com.olx.adview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.com.olx.common.AdviewNavigationModel
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

        imageLoader.loadImage(context!!, ad.images[0], image, null, null)

        price.text = ad.price
        old_price.text = ad.oldPrice
        title.text = ad.title
        published_date.text = getString(R.string.adview_publish_date, formatDate(ad.origListTime))
        description.text = ad.description
        seller_name.text = getString(R.string.adview_seller, ad.sellerName)
    }

    private fun getAd(): AdviewModel? {
        val ad = arguments?.get("ad") as? AdviewNavigationModel ?: return null

        return convertToAdviewModel(ad)
    }

}