package com.tyryshkin.newsapp.ui.news.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tyryshkin.newsapp.R

class LoadStateViewHolder(
    parent: ViewGroup,
    retry: () -> Unit
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(R.layout.load_state_item, parent, false)
) {
    private val progressBar: ProgressBar = itemView.findViewById(R.id.progress_bar)
    private val errorMsg: TextView = itemView.findViewById(R.id.error_msg)
    private val retry: Button = itemView.findViewById<Button>(R.id.retry_button)
        .also { it.setOnClickListener { retry.invoke() } }

    fun bind(loadState: LoadState) {
        progressBar.visibility = toVisibility(loadState is LoadState.Loading)
        retry.visibility = toVisibility(loadState !is LoadState.Loading)
        errorMsg.visibility = toVisibility(loadState !is LoadState.Loading)
    }

    private fun toVisibility(constraint: Boolean): Int = if (constraint) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

class NewsLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<LoadStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState) =
        LoadStateViewHolder(parent, retry)

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) =
        holder.bind(loadState)
}