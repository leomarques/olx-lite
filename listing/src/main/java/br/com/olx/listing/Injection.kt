package br.com.olx.listing

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import br.com.olx.data.Injection.provideRepository

object Injection {
    fun provideViewModelFactory(context: Context): ViewModelProvider.Factory? {
        return ViewModelFactory(provideRepository(context))
    }
}
