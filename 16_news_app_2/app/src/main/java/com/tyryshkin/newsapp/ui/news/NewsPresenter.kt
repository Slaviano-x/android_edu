package com.tyryshkin.newsapp.ui.news

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.tyryshkin.newsapp.data.network.NewsRemoteMediator
import com.tyryshkin.newsapp.data.network.ApiUtilities
import com.tyryshkin.newsapp.data.network.NewsPagingSource
import com.tyryshkin.newsapp.data.room.NewsDao
import com.tyryshkin.newsapp.data.room.NewsDatabase
import com.tyryshkin.newsapp.models.entities.News
import kotlinx.coroutines.flow.Flow

@ExperimentalPagingApi
class NewsPresenter(
    private val newsDatabase: NewsDatabase,
    private var newsView: NewsView,
    private val navigator: Navigator
) {
    private val newsDao = newsDatabase.getNewsDao()

    private val newsPagingSource: NewsPagingSource.Factory = object: NewsPagingSource.Factory {
        override fun create(): NewsPagingSource {
            return NewsPagingSource(ApiUtilities.getNewsApiService())
        }
    }

    val flow = Pager(
        PagingConfig(20, 5),
        remoteMediator = NewsRemoteMediator(newsDao, ApiUtilities.getNewsApiService())
    ) {
        newsDao.getNews()
        //newsPagingSource.create()
    }.flow

    fun updateNews() {
        Log.d("myError", "1")
        newsView.showNews()
        //newsView.showLoading(false)
    }
    /*fun onRefreshAction() {
        updateNews()
    }*/

    fun onNewsClicked(news: News) {
        //navigator.navigateToPage(news.url)
    }
}