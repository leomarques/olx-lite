package br.com.olx.data.remote

import kotlin.random.Random

fun searchRepos(
    service: AdService,
    page: Int,
    itemsPerPage: Int,
    onSuccess: (ads: List<AdRemote>) -> Unit,
    onError: (error: String) -> Unit
) {
    onSuccess(service.searchAds())
}

interface AdService {

    fun searchAds(): List<AdRemote>

    companion object {

        fun create(): AdService {
            return AdServiceMock()
        }
    }
}

class AdServiceMock : AdService {
    var lastId = 0

    override fun searchAds(): List<AdRemote> {
        val list = ArrayList<AdRemote>()
        for (i in 0..20) {
            list.add(generateNewAd())
        }
        return list
    }

    private fun generateNewAd(): AdRemote {
        val thumbs = listOf("", "", "")
        val titles = listOf("", "", "")
        val prices = listOf("", "", "")
        val dates = listOf("", "", "")
        val locations = listOf("", "", "")

        return AdRemote(
            Random.nextInt().toString(),
            thumbs[Random.nextInt(3)],
            titles[Random.nextInt(3)],
            prices[Random.nextInt(3)],
            dates[Random.nextInt(3)],
            locations[Random.nextInt(3)]
        )
    }
}
