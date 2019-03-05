package br.com.olx.android

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList

class MainActivityViewModel(private val repository: Repository) : ViewModel() {

    private val queryLiveData = MutableLiveData<String>()

    private val adResult: LiveData<AdSearchResult> = Transformations.map(queryLiveData) {
        repository.search()
    }

    val ads: LiveData<PagedList<Ad>> = Transformations.switchMap(adResult) {
        it.data
    }

    val networkErrors: LiveData<String> = Transformations.switchMap(adResult) { it.networkErrors }

    fun searchAds() {
        queryLiveData.postValue("")
    }
}