package com.ghuljr.nasaclient.di

import com.ghuljr.nasaclient.data.repository.NasaRepository
import com.ghuljr.nasaclient.data.repository.NasaRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<NasaRepository> { NasaRepositoryImpl(get()) }
}