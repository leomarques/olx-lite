package br.com.olx.android.imageloader

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide

class GlideImageLoader : ImageLoader {
    override fun loadImage(context: Context, url: String, imageView: ImageView, placeholder: Drawable?) {
//        if (placeholder != null)
//            Glide.with(context).load(url).centerCrop().placeholder(placeholder).into(imageView)
//        else
            Glide.with(context).load(url).centerCrop().into(imageView)
    }
}