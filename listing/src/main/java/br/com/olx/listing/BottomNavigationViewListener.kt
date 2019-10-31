package br.com.olx.listing

import android.view.MenuItem
import androidx.fragment.app.Fragment
import br.com.olx.common.navigateToListing
import com.google.android.material.bottomnavigation.BottomNavigationView

class BottomNavigationViewListener(private val fragment: Fragment) : BottomNavigationView.OnNavigationItemSelectedListener {

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> {
                navigateToListing(fragment)
            }
            R.id.region -> {
//                navigateToListing(fragment)
            }
            R.id.category -> {
//                navigateToListing(fragment)
            }
            R.id.filters -> {
//                navigateToListing(fragment)
            }
        }

        return true
    }
}