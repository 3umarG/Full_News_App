package com.example.newsapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.databinding.ArticleItemBinding
import com.example.newsapp.models.Article

class PagingAdapter : PagingDataAdapter<Article, PagingAdapter.NewsViewHolder>(COMPARATOR) {

    class NewsViewHolder(private val binding: ArticleItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun create(view: ViewGroup): NewsViewHolder {
                val inflater = LayoutInflater.from(view.context)
                val binding = ArticleItemBinding.inflate(inflater, view, false)
                return NewsViewHolder(binding)
            }
        }

        fun bind(article: Article) {
            binding.tvTitle.text = article.title
            binding.tvDescription.text = article.description
            binding.tvSource.text = article.source.name
            binding.tvPublishedAt.text = article.publishedAt
            Glide.with(itemView).load(article.urlToImage).placeholder(R.drawable.progress_animation)
                .error(R.drawable.ic_error).into(binding.ivArticleImage)
        }

    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val article = getItem(position)
        if (article != null) {
            holder.bind(article)

            holder.itemView.setOnClickListener {
                onItemClickListener?.let {
                    it(article)
                }
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder.create(parent)
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem == newItem
            }
        }
    }

    // This Lambda Expression return Void
    private var onItemClickListener: ((Article) -> Unit)? = null

    fun setOnItemClickListener(listener: (Article) -> Unit) {
        onItemClickListener = listener
    }
}