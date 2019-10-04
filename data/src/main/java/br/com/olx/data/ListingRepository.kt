package br.com.olx.data

import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import br.com.olx.data.local.LocalCache
import br.com.olx.data.remote.AdService

class ListingRepository(
        private val service: AdService,
        private val cache: LocalCache
) : Repository {

    override fun search(keyword: String): AdSearchResult {
        cache.clear()
        val dataSourceFactory = cache.allAds()

        service.clearPage()
        val boundaryCallback = AdBoundaryCallback(service, cache, keyword)
        val networkErrors = boundaryCallback.networkErrors
        val responseSize = boundaryCallback.responseSize
        val isRequestInProgress = boundaryCallback.isRequestInProgress

        val config: PagedList.Config = PagedList.Config.Builder()
                .setPrefetchDistance(15)
                .setPageSize(25)
                .build()

        // Get the paged list
        val data = LivePagedListBuilder(
                dataSourceFactory,
                config
        )
                .setBoundaryCallback(boundaryCallback)
                .build()

        return AdSearchResult(data, networkErrors, responseSize, isRequestInProgress)
    }
}
