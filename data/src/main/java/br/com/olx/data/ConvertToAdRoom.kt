package br.com.olx.data

import br.com.olx.data.local.AdRoom
import br.com.olx.data.remote.AdRemote

fun convertAdRemoteToAdRoom(adRemote: AdRemote): AdRoom {
    val thumbUrl = if (adRemote.thumbnail != null)
        "${adRemote.thumbnail.baseUrl}/images${adRemote.thumbnail.path}"
    else ""

    return AdRoom(
            adRemote.listId ?: "",
            thumbUrl,
            adRemote.subject ?: "",
            adRemote.price ?: "",
            adRemote.time?.toString() ?: "",
            adRemote.location?.neighbourhood ?: "",
            adRemote.oldPrice ?: "",
            adRemote.isFeatured ?: false
    )
}