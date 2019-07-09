package br.com.olx.data.remote.topazio

import br.com.olx.common.ologx
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

private val mediaType = MediaType.get("application/json")
private val client: OkHttpClient = OkHttpClient()

fun post(
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

            val responseBody = response.body() ?: return onError("Erro =(")
            val responseString = responseBody.string()
            responseBody.close()

            onSuccess(responseString)
        }
    } catch (e: Exception) {
        ologx("Request error exception")
        onError("Erro de conexão =(")
    }
}