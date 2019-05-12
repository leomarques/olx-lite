package br.com.olx.data.remote

import br.com.olx.data.remote.topazio.Location
import br.com.olx.data.remote.topazio.Thumbnail

data class AdRemote(
        val listId: String?,
        val subject: String?,
        val time: Long?,
        val price: String?,
        val location: Location?,
        val thumbnail: Thumbnail?,
        val oldPrice: String?,
        val isFeatured: Boolean?
)
