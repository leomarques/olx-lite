package br.com.olx.data.remote

import br.com.olx.data.remote.topazio.Location
import br.com.olx.data.remote.topazio.Thumbnail

data class AdRemote(
        val listId: String?,
        val subject: String?,
        val body: String?,
        val time: Long?,
        val price: String?,
        val oldPrice: String?,
        val location: Location?,
        val thumbnail: Thumbnail?,
        val isFeatured: Boolean?
)
