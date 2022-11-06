package com.example.newsapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.models.Article
import com.example.newsapp.models.NewsResponse
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response


// ViewModel Contains Repository & LiveData
class NewsViewModel(private val repository: NewsRepository) : ViewModel() {
    private var breakingNewsLiveData: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNews: LiveData<Resource<NewsResponse>> = breakingNewsLiveData
    private var pageNumber = 1

    private var searchNewsLiveData: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNews: LiveData<Resource<NewsResponse>> = searchNewsLiveData
    private var searchPage = 1

    init {
        getBreakingNews("eg")
    }

    fun getBreakingNews(country: String) = viewModelScope.launch {
        breakingNewsLiveData.postValue(Resource.Loading())
        val response = repository.getBreakingNews(country, pageNumber)

        breakingNewsLiveData.postValue(handleResponseBreakingNews(response))
    }


    fun searchNews(query: String) = viewModelScope.launch {
        searchNewsLiveData.postValue(Resource.Loading())
        val searchResponse = repository.searchNews(query, searchPage)

        searchNewsLiveData.postValue(handleResponseSearchingNews(searchResponse))
    }

    fun insertArticle(article: Article) = viewModelScope.launch {
        repository.insertArticle(article)
    }

    fun deleteArticle(article: Article) = viewModelScope.launch {
        repository.deleteArticle(article)
    }

    fun getAllArticles(): LiveData<List<Article>> = repository.getAllSaved()

    private fun handleResponseBreakingNews(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(message = response.message())
    }


    private fun handleResponseSearchingNews(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(message = response.message())
    }
}