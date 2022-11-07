package com.example.newsapp.data.remote

import com.example.newsapp.models.Article
import com.example.newsapp.utils.constants.Constants
import com.example.newsapp.models.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {

    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country") country: String = "us",
        @Query("page") page: Int = 1,
        @Query("apiKey") apiKey: String = Constants.API_KEY
    ): Response<NewsResponse>

    @GET("v2/everything")
    suspend fun getBreakingNewsPaging(
        @Query("q") query: String = "Sports",
        @Query("page") page: Int = 1,
        @Query("apiKey") apiKey: String = Constants.API_KEY
    ): Response<NewsResponse>


    @GET("v2/everything")
    suspend fun searchForNews(
        @Query("q") query : String,
        @Query("page") page: Int = 1,
        @Query("apiKey") apiKey: String = Constants.API_KEY
    ): Response<NewsResponse>
}