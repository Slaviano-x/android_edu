package com.tyryshkin.newsapp.data.network

import androidx.annotation.IntRange
import com.tyryshkin.newsapp.models.response.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {

    @GET("everything")
    suspend fun getNews(
        @Query("q") query: String,
        @Query("from") from: String,
        @Query("sortBy") sortBy: String,
        @Query("apiKey") apiKey: String,
        @Query("page")  @IntRange(from = 1, to = 5) page: Int?,
        @Query("pageSize") @IntRange(from = 1, to = MAX_PAGE_SIZE.toLong()) pageSize: Int = DEFAULT_PAGE_SIZE,
    ): Response<NewsResponse>

    companion object {
        const val DEFAULT_PAGE_SIZE = 20
        const val MAX_PAGE_SIZE = 20
    }
}