package br.com.olx.data.remote.topazio

import br.com.olx.common.ologx
import br.com.olx.data.remote.AdRemote
import br.com.olx.data.remote.AdService
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TopazioService : AdService {

    private val endpointUrl = "https://topazio.olx.com.br/"
    private var page: String? = null
    private var pageCount = 0

    override fun searchAds(keyword: String, onSuccess: (ads: List<AdRemote>, hasNextPage: Boolean) -> Unit, onError: (error: String) -> Unit, retry: Int) {
        var queryParams = "lim=25"

        if (page != null && page?.isNotEmpty() == true) {
            queryParams += "&o=$page"
        }

        if (keyword.isNotEmpty()) {
            queryParams += "&q=$keyword"
        }

        val query = buildQueryString(queryParams)

        ologx("Search Ads - nextPage: $page, pages: ${pageCount++}, retry: $retry, kw: $keyword")

        post(endpointUrl, query, { response ->
            try {
                val jsonResponse = Gson().fromJson(response, TopazioResponse::class.java)

                page = jsonResponse.data.ads.pagination

                val adRemoteList = jsonResponse.data.ads.ads.map {
                    convertToAdRemote(it)
                }

                onSuccess(adRemoteList, page?.isNotEmpty() == true)

            } catch (e: Exception) {
                ologx("Response parse exception")
                onError("Erro ao obter dados =(")
            }
        }, { error ->
            ologx("error retry $retry")
            if (retry < 3) {
                GlobalScope.launch {
                    delay(2000)
                    searchAds(keyword, onSuccess, onError, retry + 1)
                }
            } else {
                onError(error)
            }
        })
    }

    override fun clearPage() {
        page = null
        pageCount = 0
    }
}