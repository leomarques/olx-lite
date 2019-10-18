package br.com.olx.listing

import android.app.Activity
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.olx.common.FontProvider
import br.com.olx.common.imageloader.GlideImageLoader
import br.com.olx.common.navigateToAdview
import br.com.olx.common.ologx
import br.com.olx.data.DataInjection
import br.com.olx.data.local.AdRoom
import kotlinx.android.synthetic.main.listing_fragment.*

class ListingFragment : Fragment() {

    private var noResultLoaded = false
    private var preparingNewSearch = false
    private var viewWasReused = false
    private var lastRefreshTime = 0L
    private val refreshCooldownMilliseconds = 5000L
    private val imageLoader = GlideImageLoader()
    private lateinit var viewModel: ListingViewModel
    private var inflatedView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (inflatedView == null) {
            inflatedView = inflater.inflate(R.layout.listing_fragment, container, false)
            viewWasReused = false
        } else {
            viewWasReused = true
        }
        return inflatedView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setHasOptionsMenu(true)

        if (viewWasReused)
            return

        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(DataInjection.provideRepository(activity!!))
        ).get(ListingViewModel::class.java)

        adList.layoutManager = LinearLayoutManager(context)

        val adapter = AdsAdapter(imageLoader) {
            navigateToAdview(this)
        }
        adList.adapter = adapter

        viewModel.ads.observe(this, Observer<PagedList<AdRoom>> {
            ologx("Submitting Ads list, size: ${it.size}")

            adapter.submitList(it)

            showContent(it.isNotEmpty())
        })

        viewModel.responseSize.observe(this, Observer<Int> {
            preparingNewSearch = false

            showContent(it > 0)
        })

        viewModel.networkErrors.observe(this, Observer {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()

            showNewSearchLoading(false)
            showIsRefreshing(false)
            showNoResult(false)
            showList(true)
        })

        viewModel.isRequestInProgress.observe(this, Observer {
            showPagingLoading(!preparingNewSearch && it)
        })

        setPullToRefreshColor()
        pull_to_refresh.setOnRefreshListener {
            if (shouldRefresh())
                refreshAds()
            else
                showIsRefreshing(false)
        }

        searchAds("")
    }

    private fun setPullToRefreshColor() {
        val color = ContextCompat.getColor(context!!, R.color.primary_orange)
        val color2 = ContextCompat.getColor(context!!, R.color.primary_purple)
        val color3 = ContextCompat.getColor(context!!, R.color.primary_green)
        pull_to_refresh.setColorSchemeColors(color, color2, color3)
    }

    private fun showContent(isListNotEmpty: Boolean) {
        if (!preparingNewSearch) {
            if (isListNotEmpty) {
                showNewSearchLoading(false)
                showIsRefreshing(false)
                showNoResult(false)
                showList(true)
            } else {
                showNewSearchLoading(false)
                showIsRefreshing(false)
                showNoResult(true)
                showList(false)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)

        menu.clear()
        inflater?.inflate(R.menu.options_menu, menu)

        val context = ((context as AppCompatActivity).supportActionBar?.themedContext ?: context)
        val searchView = SearchView(context)
        searchView.queryHint = getString(R.string.kwsearch_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(keyword: String): Boolean {
                showNewSearchLoading(true)
                showIsRefreshing(false)
                showNoResult(false)
                showList(false)

                searchView.clearFocus()
                hideKeyboard()

                searchAds(keyword)

                return true
            }

            override fun onQueryTextChange(newText: String) = false
        })

        menu.findItem(R.id.search).apply {
            setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW or MenuItem.SHOW_AS_ACTION_IF_ROOM)
            actionView = searchView
            setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
                override fun onMenuItemActionExpand(menuItem: MenuItem) = true

                override fun onMenuItemActionCollapse(menuItem: MenuItem): Boolean {
                    showNewSearchLoading(true)
                    showIsRefreshing(false)
                    showNoResult(false)
                    showList(false)

                    searchAds("")

                    return true
                }
            })
        }
    }

    private fun hideKeyboard() {
        if (context == null || view == null)
            return

        val imm = context!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view!!.windowToken, 0)
    }

    private fun shouldRefresh(): Boolean {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastRefreshTime > refreshCooldownMilliseconds) {
            lastRefreshTime = currentTime
            return true
        }

        return false
    }

    private fun searchAds(keyWord: String) {
        preparingNewSearch = true
        viewModel.searchAds(keyWord)
    }

    private fun refreshAds() {
        preparingNewSearch = true
        viewModel.refreshAds()
    }

    private fun showList(show: Boolean) {
        adList.visibility = if (show) View.VISIBLE else View.INVISIBLE
    }

    private fun showNewSearchLoading(show: Boolean) {
        newSearchProgressBar.visibility = if (show) View.VISIBLE else View.INVISIBLE
    }

    private fun showPagingLoading(show: Boolean) {
        pagingProgressBar.visibility = if (show) View.VISIBLE else View.INVISIBLE
    }

    private fun showNoResult(show: Boolean) {
        if (show) {
            setupNoResult()
        }

        no_result.visibility = if (show) View.VISIBLE else View.INVISIBLE
    }

    private fun setupNoResult() {
        if (!noResultLoaded) {
            val url = "https://s3.amazonaws.com/static.olx.com.br/cd/android/img_noresult.png"
            imageLoader.loadImage(context!!, url, no_result_image, null, null)

            no_result_title.typeface = FontProvider.getNunitoSansRegularTypeFace(context!!)
            no_result_description.typeface = FontProvider.getNunitoSansBoldTypeFace(context!!)

            noResultLoaded = true
        }

        val keyWord = viewModel.keywordLiveData.value
        no_result_title.text = getString(R.string.no_result_title, keyWord)
    }

    private fun showIsRefreshing(show: Boolean) {
        pull_to_refresh.isRefreshing = show
    }
}
