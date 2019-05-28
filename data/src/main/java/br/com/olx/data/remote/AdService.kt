package br.com.olx.data.remote

interface AdService {
    fun searchAds(
            keyword: String,
            onSuccess: (ads: List<AdRemote>, hasNextPage: Boolean) -> Unit,
            onError: (error: String) -> Unit,
            retry: Int
    )

    fun clearPage()
}
