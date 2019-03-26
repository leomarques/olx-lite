package br.com.olx.listing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import br.com.olx.data.AdRoom
import br.com.olx.data.AdSearchResult
import br.com.olx.data.Repository

class ListingViewModel(private val repository: Repository) : ViewModel() {

    private val queryLiveData = MutableLiveData<String>()

    private val adResult: LiveData<AdSearchResult> = Transformations.map(queryLiveData) {
        repository.search()
    }

    val ads: LiveData<PagedList<AdRoom>> = Transformations.switchMap(adResult) {
        it.data
    }

    val networkErrors: LiveData<String> = Transformations.switchMap(adResult) { it.networkErrors }

    fun searchAds() {
        queryLiveData.postValue("")
    }
}
