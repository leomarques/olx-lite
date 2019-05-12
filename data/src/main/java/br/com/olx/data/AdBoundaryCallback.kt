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
        private val cache: LocalCache
) : PagedList.BoundaryCallback<AdRoom>() {

    private val _networkErrors = MutableLiveData<String>()
    // LiveData of network errors.
    val networkErrors: LiveData<String>
        get() = _networkErrors

    // avoid triggering multiple requests in the same time
    private var isRequestInProgress = false

    override fun onZeroItemsLoaded() {
        requestAndSaveData()
    }

    override fun onItemAtEndLoaded(itemAtEnd: AdRoom) {
        requestAndSaveData()
    }

    private fun requestAndSaveData() {
        if (isRequestInProgress) return

        isRequestInProgress = true

        GlobalScope.launch {
            service.searchAds(
                    { ads ->
                        ads.map { adRemote ->
                            convertAdRemoteToAdRoom(adRemote)
                        }.also {
                            cache.insert(it) {
                                isRequestInProgress = false
                            }
                        }
                    },
                    { error ->
                        _networkErrors.postValue(error)
                        isRequestInProgress = false
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
