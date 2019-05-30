package br.com.olx.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import br.com.olx.data.local.AdRoom
import br.com.olx.data.local.LocalCache
import br.com.olx.data.remote.AdRemote
import br.com.olx.data.remote.AdService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AdBoundaryCallback(
        private val service: AdService,
        private val cache: LocalCache,
        private val keyword: String
) : PagedList.BoundaryCallback<AdRoom>() {

    private var hasNextPage = true
    private val _networkErrors = MutableLiveData<String>()
    // LiveData of network errors.
    val networkErrors: LiveData<String>
        get() = _networkErrors

    private val _responseSize = MutableLiveData<Int>()
    val responseSize: LiveData<Int>
        get() = _responseSize

    // avoid triggering multiple requests in the same time
    private var isRequestInProgress = false

    override fun onZeroItemsLoaded() {
        ologx("onZeroItemsLoaded")
        requestAndSaveData()
    }

    override fun onItemAtEndLoaded(itemAtEnd: AdRoom) {
        ologx("onItemAtEndLoaded")

        if (hasNextPage)
            requestAndSaveData()
    }

    private fun requestAndSaveData() {
        if (isRequestInProgress) {
            ologx("isRequestInProgress")
            return
        }

        isRequestInProgress = true

        GlobalScope.launch {
            service.searchAds(
                    keyword,
                    { ads: List<AdRemote>, hasNextPageParam: Boolean ->
                        hasNextPage = hasNextPageParam
                        ads.map { adRemote ->
                            convertAdRemoteToAdRoom(adRemote)
                        }.also {
                            cache.insert(it) {
                                isRequestInProgress = false
                                _responseSize.postValue(it.size)
                            }
                        }
                    },
                    { error ->
                        isRequestInProgress = false
                        _responseSize.postValue(-1)
                        _networkErrors.postValue(error)
                    }, 0
            )
        }
    }

    private fun convertAdRemoteToAdRoom(adRemote: AdRemote): AdRoom {
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
}
