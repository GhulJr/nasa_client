package com.ghuljr.nasaclient.di

import android.content.Context
import com.ghuljr.nasaclient.BuildConfig
import com.ghuljr.nasaclient.data.source.remote.service.ApodService
import com.ghuljr.nasaclient.data.source.remote.service.NasaMediaService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

private const val API_KEY = "api_key"

val networkModule = module {
    single { provideCache(get()) }
    factory(named(Qualifiers.APOD)) { provideApodOkHttp(get()) }
    factory(named(Qualifiers.NASA_MEDIA)) { provideNasaApiOkHttp(get()) }
    factory { provideKotlinJsonAdapter() }
    factory { provideApodService(get(named(Qualifiers.APOD))) }
    factory { provideNasaMediaService(get(named(Qualifiers.NASA_MEDIA))) }
    single(named(Qualifiers.APOD)) { provideApodRetrofit(get(named(Qualifiers.APOD)), BuildConfig.BASE_APOD_URL, get()) }
    single(named(Qualifiers.NASA_MEDIA)) { provideApodRetrofit(get(named(Qualifiers.NASA_MEDIA)), BuildConfig.BASE_NASA_MEDIA_URL, get()) }
}

private fun provideCache(context: Context): Cache = Cache(context.cacheDir, 1024 * 1024)

private fun provideKotlinJsonAdapter(): Moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private fun provideApodOkHttp(cache: Cache): OkHttpClient  {
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

private fun provideNasaApiOkHttp(cache: Cache): OkHttpClient  {
    val loggingInterceptor = HttpLoggingInterceptor()
    loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    return OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .cache(cache)
        .build()
}


private fun provideApodRetrofit(okHttpClient: OkHttpClient, baseUrl: String, moshi: Moshi): Retrofit = Retrofit.Builder()
    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(baseUrl)
    .client(okHttpClient)
    .build()

private fun provideApodService(retrofit: Retrofit) = retrofit.create(ApodService::class.java)
private fun provideNasaMediaService(retrofit: Retrofit) = retrofit.create(NasaMediaService::class.java)
