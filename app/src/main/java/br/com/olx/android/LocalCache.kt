package br.com.olx.android

import androidx.paging.DataSource
import java.util.concurrent.Executor

class LocalCache(
    private val adDao: AdDao,
    private val ioExecutor: Executor
) {
    fun insert(ads: List<Ad>, insertFinished: () -> Unit) {
        ioExecutor.execute {
            adDao.insert(ads)
            insertFinished()
        }
    }

    fun allAds(): DataSource.Factory<Int, Ad> {
        return adDao.allAds()
    }
}
