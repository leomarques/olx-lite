package br.com.olx.data

import androidx.lifecycle.LiveData
import androidx.paging.PagedList

data class AdSearchResult(
    val data: LiveData<PagedList<AdRoom>>,
    val networkErrors: LiveData<String>
)
