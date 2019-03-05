package br.com.olx.android

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModel = ViewModelProviders.of(
            this,
            Injection.provideViewModelFactory(this)
        ).get(MainActivityViewModel::class.java)

        val decoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        adList.addItemDecoration(decoration)

        adList.layoutManager = LinearLayoutManager(this)

        val adapter = AdsAdapter()
        adList.adapter = adapter

        viewModel.ads.observe(this, Observer<PagedList<Ad>> {
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
