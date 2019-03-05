package br.com.olx.android

import android.util.Log

fun searchRepos(
    service: AdService,
    page: Int,
    itemsPerPage: Int,
    onSuccess: (ads: List<Ad>) -> Unit,
    onError: (error: String) -> Unit
) {
    onSuccess(service.searchAds())
}

interface AdService {

    fun searchAds(): List<Ad>

    companion object {

        fun create(): AdService {
            return AdServiceMock()
        }
    }
}

class AdServiceMock : AdService {
    var lastId = 0

    override fun searchAds(): List<Ad> {
        val list = ArrayList<Ad>()
        for (i in 0..50) {
            Log.d("oitdbem", i.toString())
            list.add(Ad((lastId++).toString()))
        }
        return list
    }
}