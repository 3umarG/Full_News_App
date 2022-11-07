package com.example.newsapp.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.newsapp.data.local.NewsDatabase
import com.example.newsapp.data.remote.NewsPagingSource
import com.example.newsapp.data.remote.RetrofitInstance
import com.example.newsapp.models.Article
import kotlinx.coroutines.flow.Flow

private const val PAGE_SIZE = 8

class NewsRepository(private val db: NewsDatabase) {

    suspend fun getBreakingNews(country: String, page: Int) =
        RetrofitInstance.api.getBreakingNews(country, page)

    suspend fun searchNews(query: String, page: Int) =
        RetrofitInstance.api.searchForNews(query, page)

    suspend fun insertArticle(article: Article) = db.getDao().insertArticle(article)

    suspend fun deleteArticle(article: Article) = db.getDao().deleteArticle(article)

    fun getAllSaved() = db.getDao().getAllArticles()


    fun getNewsPaging(): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                maxSize = PAGE_SIZE + (PAGE_SIZE * 2),
                enablePlaceholders = false
            ),
            pagingSourceFactory = { NewsPagingSource(RetrofitInstance.api) }
        ).flow
    }
}