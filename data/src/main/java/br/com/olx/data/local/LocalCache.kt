package br.com.olx.data.local

import androidx.paging.DataSource
import java.util.concurrent.Executor

class LocalCache(
    private val adRoomDao: AdRoomDao,
    private val ioExecutor: Executor
) {
    fun insert(ads: List<AdRoom>, insertFinished: () -> Unit) {
        ioExecutor.execute {
            adRoomDao.insert(ads.map { AdRoom(it.id) })
            insertFinished()
        }
    }

    fun allAds(): DataSource.Factory<Int, AdRoom> {
        return adRoomDao.allAds()
    }
}
