package com.example.newsapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import com.example.newsapp.databinding.FragmentArticleBinding
import com.example.newsapp.ui.viewmodel.NewsViewModel
import com.example.newsapp.utils.MainActivity
import com.google.android.material.snackbar.Snackbar

class ArticleFragment : Fragment() {
    private lateinit var binding: FragmentArticleBinding
    private lateinit var newsViewModel: NewsViewModel
    private val args: ArticleFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentArticleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var isAdded = false

        newsViewModel = (activity as MainActivity).newsViewModel
        val article = args.article

        binding.webView.apply {
            webViewClient = WebViewClient()
            article.url?.let { loadUrl(it) }
        }
        binding.fab.setOnClickListener {
            if (!isAdded) {
                newsViewModel.insertArticle(article)
                Snackbar.make(it, "Saved Article Successfully", Snackbar.LENGTH_SHORT).show()
                isAdded = true
            }else {
                Snackbar.make(it, "Article already added !!!", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

}