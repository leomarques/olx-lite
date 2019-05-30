package br.com.olx.data

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import br.com.olx.data.local.AdRoom

data class AdSearchResult(
        val data: LiveData<PagedList<AdRoom>>,
        val networkErrors: LiveData<String>,
        val responseSize: LiveData<Int>
)
