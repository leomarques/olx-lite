package br.com.olx.android.imageloader

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class GlideImageLoader : ImageLoader {

    override fun loadImage(
            context: Context,
            url: String,
            imageView: ImageView,
            placeholder: Drawable?,
            errorPlaceholder: Drawable?
    ) {
        if (placeholder != null && errorPlaceholder != null)
            Glide.with(context).
                    load(url).
                    transition(DrawableTransitionOptions.withCrossFade()).
                    placeholder(placeholder).
                    error(errorPlaceholder).
                    into(imageView)
        else
            Glide.with(context).
                    load(url).
                    transition(DrawableTransitionOptions.withCrossFade()).
                    into(imageView)
    }
}