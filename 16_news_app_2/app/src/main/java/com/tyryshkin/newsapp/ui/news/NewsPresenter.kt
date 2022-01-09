package com.tyryshkin.newsapp.ui.news

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.tyryshkin.newsapp.data.network.ApiUtilities
import com.tyryshkin.newsapp.data.network.NewsPagingSource
import com.tyryshkin.newsapp.data.room.NewsDatabase
import com.tyryshkin.newsapp.models.entities.News

@ExperimentalPagingApi
class NewsPresenter(
    newsDatabase: NewsDatabase,
    private var newsInterface: NewsInterface,
    private val navigator: Navigator,
    private val connectivityManager: ConnectivityManager
) {
    private val newsDao = newsDatabase.getNewsDao()

    private val newsPagingSource: NewsPagingSource.Factory = object: NewsPagingSource.Factory {
        override fun create(): NewsPagingSource {
            return NewsPagingSource(ApiUtilities.getNewsApiService())
        }
    }

    val flow = Pager(
        PagingConfig(20, 5)
        //remoteMediator = NewsRemoteMediator(newsDao, ApiUtilities.getNewsApiService())
    ) {
        //newsDao.getNews()
        newsPagingSource.create()
    }.flow

    fun updateNews() {
        newsInterface.showNews()
        newsInterface.showLoading(false)
    }
    fun onRefreshAction() {
        updateNews()
    }

    fun onNewsClicked(news: News) {
        navigator.navigateToPage(news.url)
    }
    fun isOnline(): Boolean {
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }
}