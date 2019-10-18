package br.com.olx.data

import br.com.olx.data.local.AdRoom
import br.com.olx.data.remote.AdRemote

fun convertAdRemoteToAdRoom(adRemote: AdRemote): AdRoom {
    val thumbUrl = if (adRemote.thumbnail != null)
        "${adRemote.thumbnail.baseUrl}/thumbs256x256${adRemote.thumbnail.path}"
    else ""

    return AdRoom(
        adRemote.listId ?: "",
        thumbUrl,
        adRemote.subject ?: "",
        adRemote.body ?: "",
        adRemote.price ?: "",
        adRemote.oldPrice ?: "",
        adRemote.time?.toString() ?: "",
        adRemote.location?.neighbourhood ?: "",
        adRemote.isFeatured ?: false,
        adRemote.sellerName ?: ""
    )
}