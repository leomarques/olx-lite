package br.com.olx.listing

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.olx.android.FontProvider
import br.com.olx.android.MainActivity
import br.com.olx.android.imageloader.GlideImageLoader
import br.com.olx.data.DataInjection
import br.com.olx.data.local.AdRoom
import br.com.olx.data.ologx
import kotlinx.android.synthetic.main.listing_fragment.*

class ListingFragment : Fragment(), MenuItemCompat.OnActionExpandListener {

    private var noResultLoaded = false
    private var lastRefreshTime = 0L
    private val refreshCooldownMiliseconds = 5000L
    private var preparingNewSearch = false
    private val imageLoader = GlideImageLoader()
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

        val adapter = AdsAdapter(imageLoader)
        adList.adapter = adapter

        viewModel.ads.observe(this, Observer<PagedList<AdRoom>> {
            ologx("ads.observe ${it.size}")

            adapter.submitList(it)

            showContent(it.isNotEmpty())
        })

        viewModel.responseSize.observe(this, Observer<Int> {
            preparingNewSearch = false

            showContent(it > 0)
        })

        viewModel.networkErrors.observe(this, Observer {
            val errorMsg = if (it.length >= 45) it.subSequence(0, 45) else it
            Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()

            showLoading(false)
            showIsRefreshing(false)
            showNoResult(false)
            showList(true)
        })

        val color = ContextCompat.getColor(context!!, br.com.olx.android.R.color.primary_orange)
        val color2 = ContextCompat.getColor(context!!, br.com.olx.android.R.color.primary_purple)
        val color3 = ContextCompat.getColor(context!!, br.com.olx.android.R.color.primary_green)
        pull_to_refresh.setColorSchemeColors(color, color2, color3)
        pull_to_refresh.setOnRefreshListener {
            if (shouldRefresh())
                refreshAds()
            else
                showIsRefreshing(false)
        }

        searchAds("")
    }

    private fun showContent(isListNotEmpty: Boolean) {
        if (!preparingNewSearch) {
            if (isListNotEmpty) {
                showLoading(false)
                showIsRefreshing(false)
                showNoResult(false)
                showList(true)
            } else {
                showLoading(false)
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
                showLoading(true)
                showList(false)

                searchView.clearFocus()
                hideKeyboard(context, view)

                searchAds(keyword)

                return true
            }

            override fun onQueryTextChange(newText: String) = false
        })
    }

    fun hideKeyboard(context: Context?, view: View?) {
        if (context == null || view == null)
            return

        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
        showLoading(true)
        showIsRefreshing(false)
        showNoResult(false)
        showList(false)

        searchAds("")

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

    private fun showLoading(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.INVISIBLE
    }

    private fun showNoResult(show: Boolean) {
        if (show) {
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

        no_result.visibility = if (show) View.VISIBLE else View.INVISIBLE
    }

    private fun showIsRefreshing(show: Boolean) {
        pull_to_refresh.isRefreshing = show
    }
}
