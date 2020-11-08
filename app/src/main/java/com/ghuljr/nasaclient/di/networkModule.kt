package com.ghuljr.nasaclient.di

import android.content.Context
import com.ghuljr.nasaclient.BuildConfig
import com.ghuljr.nasaclient.data.source.remote.NasaService
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val networkModule = module {
    single { provideCache(get()) }
    factory { provideOkHttp(get()) }
    single { provideRetrofit(get()) }
    factory { provideNasaService(get()) }
}

private fun provideCache(context: Context): Cache = Cache(context.cacheDir, 1024 * 1024)

private fun provideOkHttp(cache: Cache): OkHttpClient = OkHttpClient.Builder()
    .addInterceptor(HttpLoggingInterceptor())
    .cache(cache)
    .build()

private fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
    .baseUrl(BuildConfig.BASE_URL)
    .client(okHttpClient)
    .addConverterFactory(MoshiConverterFactory.create())
    .build()

private fun provideNasaService(retrofit: Retrofit) = retrofit.create(NasaService::class.java)