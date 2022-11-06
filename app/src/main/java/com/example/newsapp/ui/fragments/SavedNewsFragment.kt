package com.example.newsapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.R
import com.example.newsapp.adapters.NewsRecyclerAdapter
import com.example.newsapp.databinding.ActivityMainBinding
import com.example.newsapp.databinding.FragmentSavedNewsBinding
import com.example.newsapp.ui.viewmodel.NewsViewModel
import com.example.newsapp.utils.MainActivity

class SavedNewsFragment : Fragment() {
    private lateinit var binding: FragmentSavedNewsBinding
    private lateinit var newsViewModel: NewsViewModel
    private lateinit var newsRecyclerAdapter: NewsRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSavedNewsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        newsViewModel = (activity as MainActivity).newsViewModel
        setupRecyclerView()

        newsRecyclerAdapter.setOnItemClickListener { article ->
            val action =
                SavedNewsFragmentDirections.actionSavedNewsFragmentToArticleFragment(article)
            findNavController().navigate(action)
        }
    }

    private fun setupRecyclerView() {
        newsRecyclerAdapter = NewsRecyclerAdapter()
        binding.recyclerViewSavedNews.apply {
            adapter = newsRecyclerAdapter
            layoutManager = LinearLayoutManager(activity)
        }

    }


}