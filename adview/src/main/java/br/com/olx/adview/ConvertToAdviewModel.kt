package br.com.olx.adview

import br.com.olx.common.AdviewNavigationModel

fun convertToAdviewModel(ad: AdviewNavigationModel) = AdviewModel(
    listOf(ad.images[0]),
    ad.price,
    ad.oldPrice,
    ad.title,
    ad.origListTime,
    ad.description.replace("<br>", "\n"),
    ad.sellerName,
    ad.phone
)