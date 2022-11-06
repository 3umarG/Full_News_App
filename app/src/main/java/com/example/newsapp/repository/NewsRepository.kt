package com.example.newsapp.repository

import com.example.newsapp.data.local.NewsDatabase
import com.example.newsapp.data.remote.RetrofitInstance

class NewsRepository(private val db : NewsDatabase) {

    suspend fun getBreakingNews(country : String , page : Int) = RetrofitInstance.api.getBreakingNews(country , page)

    suspend fun searchNews(query : String , page: Int) = RetrofitInstance.api.searchForNews(query, page)
}