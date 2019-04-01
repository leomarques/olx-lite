package br.com.olx.data.remote

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
        for (i in 0..50) {
            list.add(AdRemote((lastId++).toString()))
        }
        return list
    }
}
