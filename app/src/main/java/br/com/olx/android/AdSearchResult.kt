package br.com.olx.android

import androidx.lifecycle.LiveData
import androidx.paging.PagedList

data class AdSearchResult(
    val data: LiveData<PagedList<Ad>>,
    val networkErrors: LiveData<String>
)
