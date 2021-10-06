package com.example.androidtest.utils

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions


fun ImageView.loadImage(context: Context, url: String) {
    val placeholder = ColorDrawable(Color.GRAY)
    val requestOptions = RequestOptions
        .placeholderOf(placeholder)
        .fitCenter()
        .optionalCenterCrop()
    Glide.with(context)
        .load(url)
        .apply(requestOptions)
        .fitCenter()
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}