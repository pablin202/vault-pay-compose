package com.pdm.vaultpay.di

import com.pdm.vaultpay.data.remote.AuthApi
import com.pdm.vaultpay.data.remote.UserApi
import com.pdm.vaultpay.network.AuthInterceptor
import com.pdm.vaultpay.utils.Constants
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    single {
        OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(androidContext()))
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = if (Constants.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
            })
            .connectTimeout(Constants.CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(Constants.READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(Constants.TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(get())
            .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()))
            .build()
    }

    single<AuthApi> {
        get<Retrofit>().create(AuthApi::class.java)
    }
    
    single<UserApi> {
        get<Retrofit>().create(UserApi::class.java)
    }
}