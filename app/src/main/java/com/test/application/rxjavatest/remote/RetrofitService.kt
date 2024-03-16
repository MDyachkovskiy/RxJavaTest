package com.test.application.rxjavatest.remote

import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitService {

    private const val BASE_URL = "https://jsonplaceholder.typicode.com/"

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }
}