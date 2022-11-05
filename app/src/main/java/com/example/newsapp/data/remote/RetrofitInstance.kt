package com.example.newsapp.data.remote

import com.example.newsapp.utils.constants.Constants
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val retrofitBuilder by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api : NewsAPI by lazy {
        retrofitBuilder.create(NewsAPI::class.java)
    }
}