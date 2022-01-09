package com.tyryshkin.newsapp.ui.news

import android.content.Context
import androidx.paging.ExperimentalPagingApi

interface NewsView {
    //fun showError(messageError: String)
    //fun showLoading(show: Boolean)
    @ExperimentalPagingApi
    fun showNews()
}
