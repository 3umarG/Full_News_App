package com.example.newsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.R
import com.example.newsapp.adapters.NewsRecyclerAdapter
import com.example.newsapp.adapters.PagingAdapter
import com.example.newsapp.databinding.FragmentBreakingNewsBinding
import com.example.newsapp.ui.viewmodel.NewsViewModel
import com.example.newsapp.utils.MainActivity
import com.example.newsapp.utils.Resource
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class BreakingNewsFragment : Fragment() {
    private lateinit var binding: FragmentBreakingNewsBinding
    private lateinit var newsViewModel: NewsViewModel
    private val pagingAdapter by lazy { PagingAdapter() }
//    private lateinit var newsRecyclerAdapter: NewsRecyclerAdapter

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
        retryButtonListener()

        lifecycleScope.launch {
            newsViewModel.pagingNews.collectLatest { pagingArticles ->
                pagingAdapter.submitData(pagingArticles)
            }
        }

/*
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

        newsRecyclerAdapter.setOnItemClickListener { article ->
            val bundle = Bundle().apply {
                putSerializable("article", article)
            }
            findNavController().navigate(
                R.id.action_breakingNewsFragment_to_articleFragment,
                bundle
            )
        }

 */
    }

    private fun retryButtonListener() {
        binding.buttonRetry.setOnClickListener {
            pagingAdapter.retry()
            pagingAdapter.addLoadStateListener { states ->
                if (states.source.refresh is LoadState.Error) {
                    Toast.makeText(
                        context,
                        "There is no internet connection yet !!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun setupRecyclerView() {
//        newsRecyclerAdapter = NewsRecyclerAdapter()

        binding.recyclerViewBreakingNews.apply {
            adapter = pagingAdapter
            layoutManager = LinearLayoutManager(activity)
        }
        adapterStateListener()
    }


    private fun adapterStateListener() {
        pagingAdapter.addLoadStateListener { loadStates ->
            when (loadStates.source.refresh) {
                is LoadState.Loading -> {
                    loadingState()
                }
                is LoadState.NotLoading -> {
                    notLoadingState()
                }
                is LoadState.Error -> {
                    errorState()
                }
            }
            handleError(loadStates)

        }

        pagingAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_breakingNewsFragment_to_articleFragment,
                bundle
            )
        }
    }

    private fun errorState() {
        binding.recyclerViewBreakingNews.visibility = View.INVISIBLE
        binding.shimmer.apply {
            stopShimmer()
            visibility = View.INVISIBLE
        }
        binding.buttonRetry.visibility = View.VISIBLE
    }

    private fun notLoadingState() {
        binding.recyclerViewBreakingNews.visibility = View.VISIBLE
        binding.shimmer.apply {
            stopShimmer()
            visibility = View.INVISIBLE
        }
        binding.buttonRetry.visibility = View.INVISIBLE
    }

    private fun loadingState() {
        binding.recyclerViewBreakingNews.visibility = View.INVISIBLE
        binding.shimmer.apply {
            visibility = View.VISIBLE
            startShimmer()
        }
        binding.buttonRetry.visibility = View.INVISIBLE
    }

    private fun handleError(loadState: CombinedLoadStates) {
        val errorState = loadState.source.append as? LoadState.Error
            ?: loadState.source.prepend as? LoadState.Error

        errorState?.let {
            Toast.makeText(context, "No Internet Connection", Toast.LENGTH_LONG).show()
        }
    }

}