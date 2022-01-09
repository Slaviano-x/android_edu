package com.tyryshkin.newsapp.data.network

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.tyryshkin.newsapp.models.entities.News
import com.tyryshkin.newsapp.models.mappers.NewsMapper
import com.tyryshkin.newsapp.util.QueryForTaskUtil
import retrofit2.HttpException
import java.io.IOException
import kotlin.streams.toList

class NewsPagingSource(
    private val newsApiService: NewsApiService
) : PagingSource<Int, News>() {

    override fun getRefreshKey(state: PagingState<Int,  News>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val anchorPage = state.closestPageToPosition(anchorPosition) ?: return null
        return anchorPage.prevKey?.plus(1) ?: anchorPage.nextKey?.minus(1)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int,  News> {

        try {
            val pageNumber = params.key ?: 1
            Log.d("anchorPage", params.key.toString())
            val pageSize = params.loadSize.coerceAtMost(NewsApiService.MAX_PAGE_SIZE)

            val response = newsApiService.getNews(
                QueryForTaskUtil.QUERY,
                QueryForTaskUtil.DATE_FROM,
                QueryForTaskUtil.SORT_OPTION,
                ApiUtilities.API_KEY,
                pageNumber,
                pageSize)

            val newsMapper = NewsMapper()
            return if (response.isSuccessful) {
                val articles = response.body()!!.getArticles()
                    .stream()
                    .filter { article -> article.title != null }
                    .map { article -> newsMapper.newsDataToEntity(article) }
                    .toList()
                val nextPageNumber = when {
                    articles.isEmpty() || pageNumber == 5 -> {
                        null
                    }
                    else -> {
                        pageNumber + 1
                    }
                }
                val prevPageNumber = if (pageNumber > 1) pageNumber - 1 else null
                //Log.d("anchorPage",  prevPageNumber.toString() + " " + nextPageNumber.toString())
                LoadResult.Page(articles, prevPageNumber, nextPageNumber)
            } else {
                LoadResult.Error(HttpException(response))
            }
        } catch (e: IOException) {
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            return LoadResult.Error(e)
        }
    }

    interface Factory {
        fun create(): NewsPagingSource
    }
}