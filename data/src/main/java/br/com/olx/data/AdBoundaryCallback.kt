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

    private var isRequestInProgress = false

    private val _networkErrors = MutableLiveData<String>()

    val networkErrors: LiveData<String>
        get() = _networkErrors

    private val _responseSize = MutableLiveData<Int>()
    val responseSize: LiveData<Int>
        get() = _responseSize

    override fun onZeroItemsLoaded() {
        requestAndSaveData()
    }

    override fun onItemAtEndLoaded(itemAtEnd: AdRoom) {
        if (hasNextPage)
            requestAndSaveData()
    }

    private fun requestAndSaveData() {
        if (isRequestInProgress) {
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
}
