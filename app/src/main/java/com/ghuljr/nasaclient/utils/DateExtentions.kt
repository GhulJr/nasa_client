package com.ghuljr.nasaclient.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter


@SuppressLint("SimpleDateFormat")
fun String.dateToTimestamp(): Long {
    return SimpleDateFormat("yyyy-MM-dd").parse(this)?.time ?: Long.MAX_VALUE
}