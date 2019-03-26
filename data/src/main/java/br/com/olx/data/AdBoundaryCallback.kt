package br.com.olx.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList

class AdBoundaryCallback(
    private val service: AdRoomService,
    private val cache: LocalCache
) : PagedList.BoundaryCallback<AdRoom>() {

    companion object {
        private const val NETWORK_PAGE_SIZE = 50
    }

    // keep the last requested page. When the request is successful, increment the page number.
    private var lastRequestedPage = 1

    private val _networkErrors = MutableLiveData<String>()
    // LiveData of network errors.
    val networkErrors: LiveData<String>
        get() = _networkErrors

    // avoid triggering multiple requests in the same time
    private var isRequestInProgress = false

    override fun onZeroItemsLoaded() {
        Log.d("oitdbem", "onZeroItemsLoaded")
        requestAndSaveData()
    }

    override fun onItemAtEndLoaded(itemAtEnd: AdRoom) {
        Log.d("oitdbem", "onItemAtEndLoaded")
        requestAndSaveData()
    }

    private fun requestAndSaveData() {
        if (isRequestInProgress) return

        isRequestInProgress = true
        searchRepos(
            service,
            lastRequestedPage,
            NETWORK_PAGE_SIZE,
            { ads ->
                cache.insert(ads) {
                    lastRequestedPage++
                    isRequestInProgress = false
                }
            },
            { error ->
                _networkErrors.postValue(error)
                isRequestInProgress = false
            })
    }
}