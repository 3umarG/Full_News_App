package com.example.newsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.R
import com.example.newsapp.adapters.NewsRecyclerAdapter
import com.example.newsapp.databinding.FragmentBreakingNewsBinding
import com.example.newsapp.ui.viewmodel.NewsViewModel
import com.example.newsapp.utils.MainActivity
import com.example.newsapp.utils.Resource

class BreakingNewsFragment : Fragment() {
    private lateinit var binding: FragmentBreakingNewsBinding
    private lateinit var newsViewModel: NewsViewModel
    private lateinit var newsRecyclerAdapter: NewsRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentBreakingNewsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        newsViewModel = (activity as MainActivity).newsViewModel
        setupRecyclerView()

        newsViewModel.breakingNews.observe(viewLifecycleOwner) { resources ->
            when (resources) {
                is Resource.Success -> {
                    hideProgressBar()
                    resources.data?.let { newsResponse ->
                        newsRecyclerAdapter.differ.submitList(newsResponse.articles)
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
                is Resource.Error -> {
                    resources.message?.let { message ->
                        Log.e("ERROR :::", message)
                    }
                }
            }

        }
    }


    private fun setupRecyclerView() {
        newsRecyclerAdapter = NewsRecyclerAdapter()
        binding.recyclerViewBreakingNews.apply {
            adapter = newsRecyclerAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
    }

}