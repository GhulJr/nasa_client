package com.ghuljr.nasaclient.utils

import android.view.View
import android.widget.ImageView
import com.squareup.picasso.Picasso

//TODO: resize images
fun ImageView.loadImage(url: String) {
    if (url.isNotEmpty()) Picasso.get().load(url).into(this)
    else this.visibility = View.GONE
}