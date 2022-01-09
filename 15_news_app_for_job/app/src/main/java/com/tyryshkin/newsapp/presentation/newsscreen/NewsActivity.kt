package com.tyryshkin.newsapp.presentation.newsscreen

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.OnReceiveContentListener
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.tyryshkin.newsapp.R
import com.tyryshkin.newsapp.data.network.NewsApiService
import com.tyryshkin.newsapp.models.entities.News
import com.tyryshkin.newsapp.presentation.common.SchedulersProvider
import java.util.*
import javax.inject.Inject

class NewsActivity : AppCompatActivity(), NewsView, Navigator{

    @Inject
    lateinit var schedulesProvider: SchedulersProvider
    @Inject
    lateinit var newsApiService: NewsApiService

    lateinit var newsPresenter: NewsPresenter

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var snackbar: Snackbar

    //TODO: final Нужен или не нужен
    companion object {
        fun getStartIntent(callingContext: Context): Intent = Intent(callingContext, NewsActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //хз зачем нужен класс newsapp
        //NewsApp.getComponentsManager().getNewsComponent().inject(this)
        setContentView(R.layout.activity_news)

        newsPresenter = NewsPresenter(this, this, newsApiService, schedulesProvider)

        //разобраться нужно или нет
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)

        swipeRefreshLayout = findViewById(R.id.news_swipe_refresh)
        swipeRefreshLayout.setOnRefreshListener { newsPresenter.updateNews(this) }
        initRecyclerView()
        newsPresenter.updateNews(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        newsPresenter.onDetachView()
        //хз зачем нужен класс newsapp
        //NewsApp.getComponentsManager().cleanNewsComponent()
    }

    fun onOptionItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    fun showNetworkError(show: Boolean) {
        if (show) {
            snackbar = Snackbar.make(
                swipeRefreshLayout,
                "Connection problems, only cashed data is shown",
                Snackbar.LENGTH_INDEFINITE).setAction("OK") { snackbar.dismiss() }
            snackbar.show()
        }
    }

    override fun showError(messageError: String) {
        snackbar = Snackbar.make(swipeRefreshLayout, "Connection problems, only cashed data is shown", Snackbar.LENGTH_LONG)
            .setAction("OK") { snackbar.dismiss() }
        snackbar.show()
    }

    override fun showLoading(show: Boolean) {
        swipeRefreshLayout.isRefreshing = show
    }

    //будто бы не используется
    override fun showNews(news: List<News>) {
        newsAdapter.setItems(news)
    }

    override fun navigateToPage(url: String) {
        val webPage = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, webPage)
        if (intent.resolveActivity(this.packageManager) != null) {
            startActivity(intent)
        } else {
            showError("Can't find a web browser. Do you have one installed?")
        }
    }

    private fun initRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView)
        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = mLayoutManager
        //TODO: создать класс наследованный от RecyclerView с методом setEmptyViewLayout
        //recyclerView.setEmptyViewLayout(R.layout.empty_news)
        //newsAdapter = NewsAdapter(news -> newsPresenter.onNewsSelected(news))
        newsAdapter = NewsAdapter()
        //40:20
        recyclerView.adapter = newsAdapter
    }
}
