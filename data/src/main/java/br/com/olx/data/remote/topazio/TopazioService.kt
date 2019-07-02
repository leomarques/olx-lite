package br.com.olx.data.remote.topazio

import br.com.olx.data.ologx
import br.com.olx.data.remote.AdRemote
import br.com.olx.data.remote.AdService
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

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

        val query = "{\"query\":\"query getAdsFromQueryString(\$queryString: String!) {    ads(queryString: \$queryString) {      pagination      ads {        listId        rankId        lastBumpAgeSecs        isFavorited        subject        origListTime        priceValue        oldPrice        professionalAd        featured        location {          neighbourhood          municipality          uf        }        images {          baseUrl          path        }        properties {          name          value        }        thumbnail {          baseUrl          path        }        user {          name          accountId        }        phone {          phone          phoneHidden          phoneVerified        }        adReply      }    }  }  \",\"variables\":{\"queryString\":\"$queryParams\"},\"operationName\":\"getAdsFromQueryString\"}"

        ologx("searchAds nextPage: $page, count: ${pageCount++}, retry: $retry, kw: $keyword")

        post(endpointUrl, query, { response ->
            try {
                val jsonResponse = Gson().fromJson<TopazioResponse>(response, TopazioResponse::class.java)

                page = jsonResponse.data.ads.pagination

                val adRemoteList = jsonResponse.data.ads.ads.map {
                    convertToAdRemote(it)
                }

                onSuccess(adRemoteList, page?.isNotEmpty() == true)

            } catch (e: Exception) {
                ologx("Response parse exception")
                onError("Error parsing response")
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

    private fun convertToAdRemote(it: Ads1): AdRemote {
        var isFeatured = false
        it.featured.forEach { item ->
            if (item == "VISUAL_FEATURED")
                isFeatured = true
        }

        return AdRemote(
                it.listId?.toString(),
                it.subject,
                it.origListTime,
                it.priceValue,
                it.location,
                it.thumbnail,
                it.oldPrice,
                isFeatured
        )
    }

    override fun clearPage() {
        page = null
        pageCount = 0
    }

    private val mediaType = MediaType.get("application/json")
    private val client: OkHttpClient = OkHttpClient()

    private fun post(
            url: String,
            json: String,
            onSuccess: (response: String) -> Unit,
            onError: (error: String) -> Unit
    ) {
        val body = RequestBody.create(mediaType, json)
        val request = Request.Builder()
                .url(url)
                .post(body)
                .build()

        try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    onError(response.message())
                }

                val responseBody = response.body() ?: return onError("Response body null")
                val responseString = responseBody.string()
                responseBody.close()

                onSuccess(responseString)
            }
        } catch (e: Exception) {
            ologx("Request error exception")
            onError("Request failed")
        }
    }
}