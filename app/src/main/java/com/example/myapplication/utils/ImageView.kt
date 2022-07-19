package com.example.myapplication.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

fun ImageView.loadImage(url: String?) {
    Glide.with(this)
        .load(url)
        .apply(RequestOptions().override(500, 500))
        .circleCrop()
        .into(this)
}