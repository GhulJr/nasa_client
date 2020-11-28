package com.ghuljr.nasaclient.di

import android.content.Context
import com.ghuljr.nasaclient.data.model.ApodModel
import com.ghuljr.nasaclient.data.model.MyObjectBox
import com.ghuljr.nasaclient.data.source.storage.StorageManager
import io.objectbox.Box
import io.objectbox.BoxStore
import io.objectbox.kotlin.boxFor
import org.koin.core.qualifier.named
import org.koin.dsl.module

enum class Qualifiers { APOD }

val storageModule = module {
    single { provideObjectBox(get()) }
    factory(named(Qualifiers.APOD)) { provideApodBox(get()) }
    single { StorageManager(get(named(Qualifiers.APOD))) }
}

fun provideObjectBox(context: Context): BoxStore {
    return MyObjectBox.builder()
        .androidContext(context.applicationContext)
        .build()
}

fun provideApodBox(boxStore: BoxStore): Box<ApodModel> = boxStore.boxFor()
