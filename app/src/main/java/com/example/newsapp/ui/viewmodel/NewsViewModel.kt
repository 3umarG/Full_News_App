package com.example.newsapp.ui.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.newsapp.models.Article
import com.example.newsapp.models.NewsResponse
import com.example.newsapp.network.ConnectivityObserver
import com.example.newsapp.network.InternetConnectionObserver
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.utils.Resource
import com.example.newsapp.utils.constants.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.Response


// ViewModel Contains Repository & LiveData
class NewsViewModel(private val repository: NewsRepository, private val context: Context) :
    ViewModel() {
    private var breakingNewsLiveData: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNews: LiveData<Resource<NewsResponse>> = breakingNewsLiveData
    private var pageNumber = 1

    private var searchNewsLiveData: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNews: LiveData<Resource<NewsResponse>> = searchNewsLiveData
    private var searchPage = 1

    val pagingNews: Flow<PagingData<Article>> = repository.getNewsPaging().cachedIn(viewModelScope)

    val connectivityObserver: ConnectivityObserver = InternetConnectionObserver(context)

    init {
//        getBreakingNews("eg")
    }

    fun getBreakingNews(country: String) = viewModelScope.launch {
        breakingNewsLiveData.postValue(Resource.Loading())
        val response = repository.getBreakingNews(country, pageNumber)

        breakingNewsLiveData.postValue(handleResponseBreakingNews(response))
    }


    fun searchNews(query: String) = viewModelScope.launch {
        connectivityObserver.observe().collect { status ->
            if (status == ConnectivityObserver.Status.AVAILABLE) {
                searchNewsLiveData.postValue(Resource.Loading())
                val searchResponse = repository.searchNews(query, searchPage)

                searchNewsLiveData.postValue(handleResponseSearchingNews(searchResponse))
            } else if (status == ConnectivityObserver.Status.UNAVAILABLE || status == ConnectivityObserver.Status.LOST) {
                searchNewsLiveData.postValue(Resource.Error(message = Constants.NO_INTERNET_CONNECTION))
            }
        }

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