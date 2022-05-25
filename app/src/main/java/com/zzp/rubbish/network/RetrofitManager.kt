package com.zzp.rubbish.network

import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitManager {
//    private const val BASE_URL = "http://101.34.85.209:22356/"
    private const val DISCERN_BASE_URL = "http://124.221.71.89:22356/"
    private const val BASE_URL = "http://120.24.60.72:8080/classification/"

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .build()

    private val discernRetrofit = Retrofit.Builder()
        .baseUrl(DISCERN_BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <T> createDiscern(serviceClass: Class<T>): T = discernRetrofit.create(serviceClass)

    inline fun <reified T> createDiscern(): T = createDiscern(T::class.java)

    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)

    inline fun <reified T> create(): T = create(T::class.java)
}