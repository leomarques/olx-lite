package br.com.olx.data.remote

interface AdService {
    fun searchAds(
            onSuccess: (ads: List<AdRemote>) -> Unit,
            onError: (error: String) -> Unit,
            retry: Int
    )

    fun clearPage()
}
