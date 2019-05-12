package br.com.olx.listing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.olx.android.imageloader.GlideImageLoader
import br.com.olx.data.Injection
import br.com.olx.data.local.AdRoom
import br.com.olx.data.ologx
import kotlinx.android.synthetic.main.listing_fragment.*

class ListingFragment : Fragment() {
    private var lastRefreshTime = 0L
    private val refreshCooldownMiliseconds = 5000L

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.listing_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val viewModel = ViewModelProviders.of(
                this,
                ViewModelFactory(Injection.provideRepository(activity!!))
        ).get(ListingViewModel::class.java)

        adList.layoutManager = LinearLayoutManager(context)

        val adapter = AdsAdapter(GlideImageLoader())
        adList.adapter = adapter

        viewModel.ads.observe(this, Observer<PagedList<AdRoom>> {
            if (it.size != 0) {
                ologx("${it.size}")
                showList(true)
                showLoading(false)
                pull_to_refresh.isRefreshing = false

                adapter.submitList(it)
            }
        })

        val color = ContextCompat.getColor(context!!, br.com.olx.android.R.color.primary_orange)
        val color2 = ContextCompat.getColor(context!!, br.com.olx.android.R.color.primary_purple)
        val color3 = ContextCompat.getColor(context!!, br.com.olx.android.R.color.primary_green)
        pull_to_refresh.setColorSchemeColors(color, color2, color3)
        pull_to_refresh.setOnRefreshListener {
            if (shouldRefresh())
                viewModel.refreshAds()
            else
                pull_to_refresh.isRefreshing = false
        }

        viewModel.networkErrors.observe(this, Observer {
            val errorMsg = if (it.length >= 15) it.subSequence(0, 15) else it
            Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
            showLoading(false)
        })

        viewModel.searchAds()
    }

    private fun shouldRefresh(): Boolean {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastRefreshTime > refreshCooldownMiliseconds) {
            ologx("will refresh, lastRefreshTime = $lastRefreshTime, currentTime = $currentTime")
            lastRefreshTime = currentTime
            return true
        }
        ologx("will NOT refresh, lastRefreshTime = $lastRefreshTime, currentTime = $currentTime")

        return false
    }

    private fun showList(show: Boolean) {
        adList.visibility = if (show) View.VISIBLE else View.INVISIBLE
    }

    private fun showLoading(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.INVISIBLE
    }
}
