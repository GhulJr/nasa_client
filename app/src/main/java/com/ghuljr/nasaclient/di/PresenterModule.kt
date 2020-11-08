package com.ghuljr.nasaclient.di

import com.ghuljr.nasaclient.ui.main.FeedPresenter
import org.koin.dsl.module

val presenterModule = module {
    single { FeedPresenter(get()) }
}