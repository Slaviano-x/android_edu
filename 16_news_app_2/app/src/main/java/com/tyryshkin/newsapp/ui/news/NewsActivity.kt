package com.tyryshkin.newsapp.ui.news

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.tyryshkin.newsapp.R
import com.tyryshkin.newsapp.models.entities.News
import com.tyryshkin.newsapp.ui.web.WebActivity
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import com.tyryshkin.newsapp.data.room.NewsDatabase
import com.tyryshkin.newsapp.ui.news.adapters.NewsAdapter
import com.tyryshkin.newsapp.ui.news.adapters.NewsLoadStateAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch

@ExperimentalPagingApi
class NewsActivity : AppCompatActivity(), NewsView, Navigator {

    private lateinit var recyclerView: RecyclerView
    private lateinit var snackbar: Snackbar
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private lateinit var newsAdapter: NewsAdapter

    private lateinit var newsPresenter: NewsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_main)

        newsPresenter = NewsPresenter(NewsDatabase.getInstance(this),this, this)

        initViews()
        initRecyclerView()

        newsPresenter.updateNews()
    }


    /*override fun showError(messageError: String) {
        snackbar = Snackbar.make(swipeRefreshLayout, messageError, Snackbar.LENGTH_INDEFINITE)
            .setAction("OK") { snackbar.dismiss() }
        snackbar.show()
    }*/

    /*override fun showLoading(show: Boolean) {
        swipeRefreshLayout.isRefreshing = show
    }*/


    override fun showNews() {
        lifecycleScope.launch {
            newsPresenter.flow.collectLatest{
                newsAdapter.submitData(it)
            }
        }
    }

    /*override fun navigateToPage(url: String?) {
        val intent = Intent(this, WebActivity::class.java)
        intent.putExtra("url", url)
        startActivity(intent)
    }*/

    private fun initViews() {
        recyclerView = findViewById(R.id.recyclerView)

        /*swipeRefreshLayout = findViewById(R.id.news_swipe_refresh)
        swipeRefreshLayout.setOnRefreshListener { newsPresenter.onRefreshAction() }*/
    }

    private fun initRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        //TODO: создать класс наследованный от RecyclerView с методом setEmptyViewLayout
        newsAdapter = NewsAdapter(object : NewsAdapter.OnNewsClickListener {
            override fun onNewsClicked(news: News) {
                newsPresenter.onNewsClicked(news)
            }
        })
        recyclerView.adapter = newsAdapter.withLoadStateFooter(
            footer = NewsLoadStateAdapter(newsAdapter::retry)
        )
    }

}