package br.com.olx.listing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import br.com.olx.data.AdSearchResult
import br.com.olx.data.Repository
import br.com.olx.data.local.AdRoom

class ListingViewModel(private val repository: Repository) : ViewModel() {

    private val keywordLiveData = MutableLiveData<String>()

    private val adSearchResult: LiveData<AdSearchResult> = Transformations.map(keywordLiveData) {
        repository.search(it)
    }

    val ads: LiveData<PagedList<AdRoom>> = Transformations.switchMap(adSearchResult) {
        it.data
    }

    val networkErrors: LiveData<String> = Transformations.switchMap(adSearchResult) { it.networkErrors }

    fun searchAds(keyword: String) {
        keywordLiveData.postValue(keyword)
    }

    fun refreshAds() {
        val keyword = keywordLiveData.value ?: ""
        keywordLiveData.postValue(keyword)
    }
}
