package com.ghuljr.nasaclient.utils

import android.widget.ImageView
import com.squareup.picasso.Picasso

//TODO: resize images
fun ImageView.loadImage(url: String) {
    Picasso.get().load(url).into(this)
}