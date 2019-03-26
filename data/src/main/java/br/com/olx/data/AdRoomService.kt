package br.com.olx.data

fun searchRepos(
    service: AdRoomService,
    page: Int,
    itemsPerPage: Int,
    onSuccess: (ads: List<AdRoom>) -> Unit,
    onError: (error: String) -> Unit
) {
    onSuccess(service.searchAds())
}

interface AdRoomService {

    fun searchAds(): List<AdRoom>

    companion object {

        fun create(): AdRoomService {
            return AdServiceMock()
        }
    }
}

class AdServiceMock : AdRoomService {
    var lastId = 0

    override fun searchAds(): List<AdRoom> {
        val list = ArrayList<AdRoom>()
        for (i in 0..50) {
            list.add(AdRoom((lastId++).toString()))
        }
        return list
    }
}