package com.tyryshkin.newsapp.ui.news

import androidx.paging.ExperimentalPagingApi

interface NewsInterface {
    fun showLoading(show: Boolean)
    @ExperimentalPagingApi
    fun showNews()
}
