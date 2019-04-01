package br.com.olx.listing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.olx.data.local.AdRoom
import kotlinx.android.synthetic.main.listing_fragment.*

class ListingFragment : Fragment() {

    companion object {
        fun newInstance() = ListingFragment()
    }

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
            Injection.provideViewModelFactory(activity!!)
        ).get(ListingViewModel::class.java)

        val decoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        adList.addItemDecoration(decoration)

        adList.layoutManager = LinearLayoutManager(context)

        val adapter = AdsAdapter()
        adList.adapter = adapter

        viewModel.ads.observe(this, Observer<PagedList<AdRoom>> {
            showEmptyList(it?.size == 0)
            adapter.submitList(it)
        })

        viewModel.searchAds()
    }

    private fun showEmptyList(show: Boolean) {
        if (show) {
            emptyList.visibility = View.VISIBLE
            adList.visibility = View.GONE
        } else {
            emptyList.visibility = View.GONE
            adList.visibility = View.VISIBLE
        }
    }
}
