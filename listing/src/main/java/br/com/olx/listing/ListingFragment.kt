package br.com.olx.listing

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.olx.android.MainActivity
import br.com.olx.android.imageloader.GlideImageLoader
import br.com.olx.data.DataInjection
import br.com.olx.data.local.AdRoom
import br.com.olx.data.ologx
import kotlinx.android.synthetic.main.listing_fragment.*

class ListingFragment : Fragment(), MenuItemCompat.OnActionExpandListener {

    private var lastRefreshTime = 0L
    private val refreshCooldownMiliseconds = 5000L

    private lateinit var viewModel: ListingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
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

        viewModel = ViewModelProviders.of(
                this,
                ViewModelFactory(DataInjection.provideRepository(activity!!))
        ).get(ListingViewModel::class.java)

        adList.layoutManager = LinearLayoutManager(context)

        val adapter = AdsAdapter(GlideImageLoader())
        adList.adapter = adapter

        viewModel.ads.observe(this, Observer<PagedList<AdRoom>> {
            ologx("${it.size}")
            showLoading(false)
            showIsRefreshing(false)

            if (it.size != 0) {
                showList(true)
                showNoResult(false)

                adapter.submitList(it)
            } else {
                showList(false)
                showNoResult(true)
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
                showIsRefreshing(false)
        }

        viewModel.networkErrors.observe(this, Observer {
            val errorMsg = if (it.length >= 45) it.subSequence(0, 45) else it
            Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()

            showLoading(false)
            showIsRefreshing(false)
            showList(true)
            showNoResult(false)
        })

        viewModel.searchAds("")
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)

        menu.clear()
        inflater?.inflate(R.menu.options_menu, menu)

        val context = ((context as MainActivity).supportActionBar?.themedContext ?: context)
        val searchView = SearchView(context)
        searchView.queryHint = getString(R.string.kwsearch_hint)

        val menuItem = menu.findItem(R.id.search)
        menuItem.apply {
            setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW or MenuItem.SHOW_AS_ACTION_IF_ROOM)
            actionView = searchView
        }

        // When using the support library, the setOnActionExpandListener() method is
        // static and accepts the MenuItem object as an argument
        MenuItemCompat.setOnActionExpandListener(menuItem, this)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(keyword: String): Boolean {
                viewModel.searchAds(keyword)
                showList(false)
                showLoading(true)

                return false
            }

            override fun onQueryTextChange(newText: String) = false
        })
    }

    override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
        showList(false)
        showLoading(true)
        showNoResult(false)
        showIsRefreshing(false)
        viewModel.searchAds("")

        return true
    }

    override fun onMenuItemActionExpand(item: MenuItem?) = true

    private fun shouldRefresh(): Boolean {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastRefreshTime > refreshCooldownMiliseconds) {
            lastRefreshTime = currentTime
            return true
        }

        return false
    }

    private fun showList(show: Boolean) {
        adList.visibility = if (show) View.VISIBLE else View.INVISIBLE
    }

    private fun showLoading(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.INVISIBLE
    }

    private fun showNoResult(show: Boolean) {
        no_result.visibility = if (show) View.VISIBLE else View.INVISIBLE
    }

    private fun showIsRefreshing(show: Boolean) {
        pull_to_refresh.isRefreshing = show
    }
}
