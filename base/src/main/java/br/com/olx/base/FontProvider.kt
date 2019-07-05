package br.com.olx.base

import android.content.Context
import androidx.core.content.res.ResourcesCompat

class FontProvider {
    companion object {
        fun getNunitoSansRegularTypeFace(context: Context) = ResourcesCompat.getFont(context, R.font.nunitosans_regular)
        fun getNunitoSansBoldTypeFace(context: Context) = ResourcesCompat.getFont(context, R.font.nunitosans_bold)
    }
}