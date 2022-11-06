package com.example.newsapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.adapters.NewsRecyclerAdapter
import com.example.newsapp.databinding.ActivityMainBinding
import com.example.newsapp.databinding.FragmentSavedNewsBinding
import com.example.newsapp.ui.viewmodel.NewsViewModel
import com.example.newsapp.utils.MainActivity
import com.google.android.material.snackbar.Snackbar

class SavedNewsFragment : Fragment() {
    private lateinit var binding: FragmentSavedNewsBinding
    private lateinit var newsViewModel: NewsViewModel
    private lateinit var newsRecyclerAdapter: NewsRecyclerAdapter
    private val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.UP or ItemTouchHelper.DOWN,
        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            // TODO : our functionality
            swipe(viewHolder)

        }
    }

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

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.recyclerViewSavedNews)
        }

        onArticleClick()
        observing()


    }

    private fun setupRecyclerView() {
        newsRecyclerAdapter = NewsRecyclerAdapter()
        binding.recyclerViewSavedNews.apply {
            adapter = newsRecyclerAdapter
            layoutManager = LinearLayoutManager(activity)
        }

    }

    private fun observing() {
        newsViewModel.getAllArticles().observe(viewLifecycleOwner) { articles ->
            newsRecyclerAdapter.differ.submitList(articles)
        }
    }

    private fun onArticleClick() {
        newsRecyclerAdapter.setOnItemClickListener { article ->
            val bundle = Bundle().apply {
                putSerializable("article", article)
            }
            findNavController().navigate(
                R.id.action_savedNewsFragment_to_articleFragment,
                bundle
            )
        }
    }

    private fun swipe(viewHolder: RecyclerView.ViewHolder) {
        val position = viewHolder.adapterPosition
        val article = newsRecyclerAdapter.differ.currentList[position]
        newsViewModel.deleteArticle(article)
        Snackbar.make(requireView(), "Article Deleted !!", Snackbar.LENGTH_LONG).apply {
            setAction("UNDO") {
                newsViewModel.insertArticle(article)
            }
            show()
        }
    }

}