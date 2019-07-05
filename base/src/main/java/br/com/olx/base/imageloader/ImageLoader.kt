package br.com.olx.base.imageloader

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView

interface ImageLoader {
    fun loadImage(
            context: Context,
            url: String,
            imageView: ImageView,
            placeholder: Drawable?,
            errorPlaceholder: Drawable?)
}