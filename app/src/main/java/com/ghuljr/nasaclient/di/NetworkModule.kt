package com.ghuljr.nasaclient.di

import android.content.Context
import com.ghuljr.nasaclient.BuildConfig
import com.ghuljr.nasaclient.data.source.remote.NasaService
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

val networkModule = module {
    single { provideCache(get()) }
    factory { provideOkHttp(get()) }
    single { provideRetrofit(get()) }
    factory { provideNasaService(get()) }
}

private fun provideCache(context: Context): Cache = Cache(context.cacheDir, 1024 * 1024)

private fun provideOkHttp(cache: Cache): OkHttpClient  {
    val loggingInterceptor = HttpLoggingInterceptor()
    loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    return OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor { chain ->
            val original = chain.request()

            val url = original.url.newBuilder()
                .addQueryParameter(API_KEY, BuildConfig.API_KEY)
                .build()

            val request = original.newBuilder()
                .url(url)
                .build()

            return@addInterceptor chain.proceed(request)
        }
        .cache(cache)
        .build()
}

private fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create())
    .baseUrl(BuildConfig.BASE_URL)
    .client(okHttpClient)
    .build()

private fun provideNasaService(retrofit: Retrofit) = retrofit.create(NasaService::class.java)

private const val API_KEY = "api_key"