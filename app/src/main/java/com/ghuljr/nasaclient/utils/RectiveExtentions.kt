package com.ghuljr.nasaclient.utils

import android.view.View
import com.jakewharton.rxbinding4.view.clicks
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit

fun View.reactiveClick(): Observable<Unit> = this.clicks().throttleFirst(500L, TimeUnit.MILLISECONDS)