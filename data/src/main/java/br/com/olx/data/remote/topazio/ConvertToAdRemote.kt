package br.com.olx.data.remote.topazio

import br.com.olx.data.remote.AdRemote

fun convertToAdRemote(it: Ads1): AdRemote {
    var isFeatured = false
    it.featured.forEach { item ->
        if (item == "VISUAL_FEATURED")
            isFeatured = true
    }

    return AdRemote(
        it.listId?.toString(),
        it.subject,
        it.body,
        it.origListTime,
        it.priceValue,
        it.oldPrice,
        it.location,
        it.thumbnail,
        isFeatured,
        it.user?.name
    )
}