package br.com.olx.android

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import java.util.concurrent.Executors

object Injection {
    private fun provideCache(context: Context): LocalCache {
        val database = AdDatabase.getInstance(context)
        return LocalCache(database.adsDao(), Executors.newSingleThreadExecutor())
    }

    private fun provideRepository(context: Context): Repository {
        return ListingRepository(AdService.create(), provideCache(context))
    }

    fun provideViewModelFactory(context: Context): ViewModelProvider.Factory? {
        return ViewModelFactory(provideRepository(context))
    }
}