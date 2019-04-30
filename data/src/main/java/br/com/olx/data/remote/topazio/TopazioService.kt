package br.com.olx.data.remote.topazio

import br.com.olx.data.ologx
import br.com.olx.data.remote.AdRemote
import br.com.olx.data.remote.AdService
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

class TopazioService : AdService {

    private val endpointUrl = "http://advil-router.us-east-1.elasticbeanstalk.com/"
    private var page: String? = null
    private var pageCount = 0

    override fun searchAds(onSuccess: (ads: List<AdRemote>) -> Unit, onError: (error: String) -> Unit) {
        page = if (page == null) {
            ""
        } else {
            "o=$page"
        }

        val query = "{\"query\":\"query getAdsFromQueryString(\$queryString: String!) {    ads(queryString: \$queryString) {      pagination      ads {        listId        rankId        lastBumpAgeSecs        isFavorited        subject        origListTime        priceValue        oldPrice        professionalAd        featured        location {          neighbourhood          municipality          uf        }        images {          baseUrl          path        }        properties {          name          value        }        thumbnail {          baseUrl          path        }        user {          name          accountId        }        phone {          phone          phoneHidden          phoneVerified        }        adReply      }    }  }  \",\"variables\":{\"queryString\":\"$page&lim=25\"},\"operationName\":\"getAdsFromQueryString\"}"

        ologx("searchAds $page - count: ${pageCount++}")

        post(endpointUrl, query, { response ->
            try {
                val jsonResponse = Gson().fromJson<TopazioResponse>(response, TopazioResponse::class.java)

                page = jsonResponse.data.ads.pagination

                val adRemoteList = jsonResponse.data.ads.ads.map {
                    convertToAdRemote(it)
                }
                onSuccess(adRemoteList)

            } catch (e: Exception) {
                ologx("Response parse exception")
                onError("Error parsing response")
            }
        }, { error ->
            onError(error)
        })
    }

    private fun convertToAdRemote(it: Ads1): AdRemote {
        return AdRemote(
                it.listId?.toString(),
                it.subject,
                it.origListTime,
                it.priceValue,
                it.location,
                it.thumbnail,
                it.oldPrice
        )
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