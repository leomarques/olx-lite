package br.com.olx.common

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.findNavController

fun navigateToAdview(
    ad: AdviewNavigationModel,
    fragment: Fragment
) {
    val bundle = Bundle().apply { putSerializable("ad", ad) }
    findNavController(fragment).navigate(R.id.action_listing_to_adview, bundle)
}