package br.com.olx.data

import androidx.paging.LivePagedListBuilder
import br.com.olx.data.local.LocalCache
import br.com.olx.data.remote.AdService

class ListingRepository(
        private val service: AdService,
        private val cache: LocalCache
) : Repository {

    override fun search(): AdSearchResult {
        val dataSourceFactory = cache.allAds()

        // Construct the boundary callback
        val boundaryCallback = AdBoundaryCallback(service, cache)
        val networkErrors = boundaryCallback.networkErrors

        // Get the paged list
        val data = LivePagedListBuilder(
                dataSourceFactory,
                DATABASE_PAGE_SIZE
        )
                .setBoundaryCallback(boundaryCallback)
                .build()

        // Get the network errors exposed by the boundary callback
        return AdSearchResult(data, networkErrors)
    }

    companion object {
        private const val DATABASE_PAGE_SIZE = 25
    }
}
