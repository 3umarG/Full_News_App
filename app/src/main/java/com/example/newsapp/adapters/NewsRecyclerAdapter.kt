package com.example.newsapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.models.Article

class NewsRecyclerAdapter(private val listener: OnItemClickListener) :
    RecyclerView.Adapter<NewsRecyclerAdapter.NewsViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallback)


    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        private val tvDescription = itemView.findViewById<TextView>(R.id.tvDescription)
        private val tvSource = itemView.findViewById<TextView>(R.id.tvSource)
        private val tvPublishedAt = itemView.findViewById<TextView>(R.id.tvPublishedAt)
        private val ivArticleImage = itemView.findViewById<ImageView>(R.id.ivArticleImage)

        fun bind(article: Article) {
            tvTitle.text = article.title
            tvDescription.text = article.description
            tvSource.text = article.source.name
            tvPublishedAt.text = article.publishedAt
            Glide.with(itemView).load(article.urlToImage).placeholder(R.drawable.progress_animation)
                .into(ivArticleImage)
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.article_item, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
        holder.itemView.setOnClickListener {
            listener.onItemClick(differ.currentList[position])
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    interface OnItemClickListener {
        fun onItemClick(article: Article)
    }

}