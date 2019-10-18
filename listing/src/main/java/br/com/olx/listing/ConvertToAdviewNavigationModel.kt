package br.com.olx.listing

import br.com.olx.common.AdviewNavigationModel
import br.com.olx.data.local.AdRoom

fun convertToAdviewNavigationModel(adRoom: AdRoom) = AdviewNavigationModel(
    listOf(adRoom.thumbUrl),
    adRoom.price,
    adRoom.oldPrice,
    adRoom.title,
    adRoom.date,
    adRoom.description,
    adRoom.sellerName,
    "phone"
)