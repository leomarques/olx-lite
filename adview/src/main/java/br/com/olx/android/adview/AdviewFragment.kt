package br.com.olx.android.adview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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

        imageLoader.loadImage(context!!, ad.images[0], image, null, null)

        price.text = ad.price
        old_price.text = ad.oldPrice
        title.text = ad.title
        published_date.text = ad.origListTime
        description.text = ad.description
        seller_name.text = getString(R.string.adview_seller, ad.sellerName)
    }

    private fun getAd() = AdviewModel(
        listOf("https://img.olx.com.br/images/13/134905038475847.jpg"),
        "R$119.900",
        "R$12.345",
        "Volkswagen Golf GTI 4P",
        "Publicado em 16/10 ás 17:09",
        "vale bem menos do q vc pensa",
        "Aristarco Pederneiras",
        "997339174"
    )

}