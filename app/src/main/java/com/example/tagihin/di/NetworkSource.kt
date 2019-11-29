package com.example.tagihin.di

import com.example.tagihin.BuildConfig
import com.example.tagihin.data.remote.ApiService
import com.example.tagihin.utils.Consts
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

val remoteDataSourceModule = module {
    single { provideOkHttpClient() }
    single { provideRetrofit<ApiService>(get()) }
}

private fun getLogInterceptor() = HttpLoggingInterceptor().apply {
    level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
}

fun provideOkHttpClient(): OkHttpClient {
    val okHttpClient = OkHttpClient.Builder()
    okHttpClient.connectTimeout(Consts.CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
    okHttpClient.hostnameVerifier { _, _ -> true }
    okHttpClient.readTimeout(Consts.READ_TIMEOUT, TimeUnit.MILLISECONDS)
    okHttpClient.writeTimeout(Consts.WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
    okHttpClient.addInterceptor(getLogInterceptor())
    return okHttpClient.build()
}

inline fun <reified T> provideRetrofit(okhttpclient: OkHttpClient): T {
    val retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.SERVER_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(okhttpclient)
        .build()

    return retrofit.create(T::class.java)
}