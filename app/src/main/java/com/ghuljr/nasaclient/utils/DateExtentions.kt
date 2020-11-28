package com.ghuljr.nasaclient.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*


@SuppressLint("SimpleDateFormat")
fun String.dateToTimestamp(): Long {
    return SimpleDateFormat("yyyy-MM-dd").parse(this)?.time ?: Long.MAX_VALUE
}

fun String.isDateExpired(timeOffset: Long = 0): Boolean {
    return currentTimestamp() - this.dateToTimestamp() > timeOffset
}

fun Any.currentTimestamp(): Long = Calendar.getInstance().timeInMillis
