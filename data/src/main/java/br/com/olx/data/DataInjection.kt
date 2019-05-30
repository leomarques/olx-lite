package br.com.olx.data

import android.content.Context
import br.com.olx.data.local.AdRoomDatabase
import br.com.olx.data.local.LocalCache
import br.com.olx.data.remote.AdService
import br.com.olx.data.remote.topazio.TopazioService
import java.util.concurrent.Executors

object DataInjection {
    private fun provideCache(context: Context): LocalCache {
        val database = AdRoomDatabase.getInstance(context)
        return LocalCache(database.adsDao(), Executors.newSingleThreadExecutor())
    }

    fun provideRepository(context: Context): Repository {
        return ListingRepository(
            provideAdService(),
            provideCache(context)
        )
    }

    private fun provideAdService(): AdService {
        return TopazioService()
    }
}
