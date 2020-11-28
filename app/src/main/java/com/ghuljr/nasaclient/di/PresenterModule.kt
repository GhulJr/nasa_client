package com.ghuljr.nasaclient.di

import com.ghuljr.nasaclient.ui.apod.ApodDetailsPresenter
import com.ghuljr.nasaclient.ui.main.FeedPresenter
import com.ghuljr.nasaclient.ui.splash.SplashPresenter
import org.koin.dsl.module

val presenterModule = module {
    factory { FeedPresenter(get()) }
    factory { SplashPresenter(get()) }
    factory { ApodDetailsPresenter(get()) }
}