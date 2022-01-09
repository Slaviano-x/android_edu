package com.tyryshkin.newsapp.ui.news

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tyryshkin.newsapp.R
import com.tyryshkin.newsapp.models.entities.News
import com.tyryshkin.newsapp.ui.web.WebActivity
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import com.tyryshkin.newsapp.data.room.NewsDatabase
import com.tyryshkin.newsapp.ui.news.adapters.NewsAdapter
import com.tyryshkin.newsapp.ui.news.adapters.NewsLoadStateAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@ExperimentalPagingApi
class NewsActivity : AppCompatActivity(), NewsInterface, Navigator {

    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var constraintLayout: ConstraintLayout
    private lateinit var textView: TextView
    private lateinit var retryButton: Button

    private lateinit var newsAdapter: NewsAdapter

    private lateinit var newsPresenter: NewsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_main)

        newsPresenter = NewsPresenter(
            NewsDatabase.getInstance(this),
            this,
            this,
            getConnectivityManager())

        initViews()
        initRecyclerView()

        if (newsPresenter.isOnline()) {
            showActivity()
            newsPresenter.updateNews()
        } else {
            showActivity(View.INVISIBLE, View.VISIBLE, getString(R.string.error_internet_2))
        }
    }

    override fun showLoading(show: Boolean) {
        swipeRefreshLayout.isRefreshing = show
    }


    override fun showNews() {
        lifecycleScope.launch {
            newsPresenter.flow.collectLatest{
                newsAdapter.submitData(it)
            }
        }
    }

    override fun navigateToPage(url: String?) {
        val intent = Intent(this, WebActivity::class.java)
        intent.putExtra("url", url)
        startActivity(intent)
    }

    private fun initViews() {
        recyclerView = findViewById(R.id.recyclerView)

        swipeRefreshLayout = findViewById(R.id.news_swipe_refresh)
        swipeRefreshLayout.setOnRefreshListener { newsPresenter.onRefreshAction() }
        constraintLayout = findViewById(R.id.constraintLayout)
        textView = findViewById(R.id.textView3)

        retryButton = findViewById(R.id.button)
        retryButton.setOnClickListener {
            showActivity()
            finish()
            startActivity(intent)
        }
    }

    private fun initRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        newsAdapter = NewsAdapter(object : NewsAdapter.OnNewsClickListener {
            override fun onNewsClicked(news: News) {
                newsPresenter.onNewsClicked(news)
            }
        })
        recyclerView.adapter = newsAdapter.withLoadStateFooter(
            footer = NewsLoadStateAdapter(newsAdapter::retry)
        )
    }
    private fun getConnectivityManager(): ConnectivityManager {
        return this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    private fun showActivity(visibleRecyclerView: Int = View.VISIBLE,
                             visibleLayout: Int = View.INVISIBLE,
                             errorMsg: String = "") {
        swipeRefreshLayout.visibility = visibleRecyclerView
        constraintLayout.visibility = visibleLayout
        textView.text = errorMsg
    }
}