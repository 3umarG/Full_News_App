package com.example.newsapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.newsapp.repository.NewsRepository


// ViewModel Contains Repository & LiveData
class NewsViewModel(val repository: NewsRepository) : ViewModel()
{
}