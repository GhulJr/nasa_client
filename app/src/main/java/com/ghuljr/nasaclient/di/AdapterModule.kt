package com.ghuljr.nasaclient.di

import com.ghuljr.nasaclient.ui.main.ApodAdapter
import org.koin.dsl.module

val adapterModule = module {
    factory { ApodAdapter() }
}