package br.com.olx.common

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.findNavController

fun navigateToAdview(fragment: Fragment) {
    findNavController(fragment).navigate(R.id.action_listing_to_adview)
}