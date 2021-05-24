package com.spacex.tech

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("srcUrl", requireAll = false)
fun ImageView.bindSrcUrl(
    url: String
) {
    val request = Glide.with(this).load(url)
    request.into(this)
}