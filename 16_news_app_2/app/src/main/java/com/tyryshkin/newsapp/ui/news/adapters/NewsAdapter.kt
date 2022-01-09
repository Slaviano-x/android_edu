package com.tyryshkin.newsapp.ui.news.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tyryshkin.newsapp.R
import com.tyryshkin.newsapp.models.entities.News
import java.text.SimpleDateFormat

class NewsAdapter (private val listener: OnNewsClickListener) :
    PagingDataAdapter<News, NewsAdapter.NewsViewHolder>(NewsDiffItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_news, parent, false)

        return NewsViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    class NewsViewHolder(
        itemView: View,
        private val listener: OnNewsClickListener
        ) : RecyclerView.ViewHolder(itemView) {

        private val imageImageView: ImageView = itemView.findViewById(R.id.imageImageView)
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        private val dataPublishedAtTextView: TextView = itemView.findViewById(R.id.dataPublishedAtTextView)

        @SuppressLint("SimpleDateFormat")
        fun bind(news: News) {
            itemView.setOnClickListener { listener.onNewsClicked(news) }
            Picasso.get().load(news.urlToImage).into(imageImageView)
            titleTextView.text = news.title
            descriptionTextView.text = news.description
            val parser =  SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            val formatter = SimpleDateFormat("dd.MM.yyyy")
            try {
                dataPublishedAtTextView.text = formatter.format(parser.parse(news.dataPublishedAt))
            } catch (e: NullPointerException) {
                dataPublishedAtTextView.text = ""
            }
        }
    }

    interface OnNewsClickListener {
        fun onNewsClicked(news: News)
    }
}

class NewsDiffItemCallback : DiffUtil.ItemCallback<News>() {

    override fun areItemsTheSame(oldItem: News, newItem: News): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: News, newItem: News): Boolean {
        return oldItem.title == newItem.title && oldItem.url == newItem.url
    }
}