package com.tyryshkin.newsapp.data.network

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.tyryshkin.newsapp.data.room.NewsDao
import com.tyryshkin.newsapp.data.room.RemoteKey
import com.tyryshkin.newsapp.models.entities.News
import com.tyryshkin.newsapp.models.mappers.NewsMapper
import com.tyryshkin.newsapp.util.QueryForTaskUtil

@ExperimentalPagingApi
class NewsRemoteMediator(
    private val newsDao: NewsDao,
    private val newsApiService: NewsApiService
) : RemoteMediator<Int, News>() {

    override suspend fun initialize(): InitializeAction {
        Log.d("remoteKey", "LAUNCH_INITIAL_REFRESH")
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, News>): MediatorResult {
        Log.d("remoteKey", "load")
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    Log.d("remoteKey", "REFRESH")
                    val remoteKey = getClosestRemoteKeys(state)
                    remoteKey?.nextKey?.minus(1) ?: 1
                }
                LoadType.PREPEND -> {
                    Log.d("remoteKey", "PREPEND")
                    return MediatorResult.Success(endOfPaginationReached = true)}
                LoadType.APPEND -> {
                    Log.d("remoteKey", "APPEND")
                    val remoteKey = getLastRemoteKey(state)
                    //Log.d("remoteKeyTT", remoteKey!!.nextKey.toString())
                    remoteKey?.nextKey
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
            }
            Log.d("anchorPage", page.toString())
            val response = newsApiService.getNews(
                QueryForTaskUtil.QUERY,
                QueryForTaskUtil.DATE_FROM,
                QueryForTaskUtil.SORT_OPTION,
                ApiUtilities.API_KEY,
                page,
                state.config.pageSize
            )

            if (response.isSuccessful) {
                response.body()?.let {
                    if (loadType == LoadType.REFRESH) {
                        newsDao.deleteNews()
                        newsDao.deleteAllRemoteKeys()
                    }
                    val newsMapper = NewsMapper()
                    val articles = response.body()!!.getArticles()
                        .map { article -> newsMapper.newsDataToEntity(article) }

                    val prevPageNumber = if (page == 1) null else page - 1
                    val nextPageNumber = if (articles.isEmpty() || page == 5) null else page + 1
                    Log.d("anchorPage", prevPageNumber.toString() + " " + nextPageNumber.toString())
                    val keys = articles.map {
                        RemoteKey(it.title, prevKey = prevPageNumber, nextKey = nextPageNumber)
                    }

                    newsDao.insertNews(articles)
                    newsDao.insertAllRemoteKeys(keys)
                }
                MediatorResult.Success(endOfPaginationReached = page == 5)
            } else {
                MediatorResult.Success(endOfPaginationReached = true)
            }
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getClosestRemoteKeys(state: PagingState<Int, News>): RemoteKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.let { news ->
                newsDao.remoteKeysId(news.title)
            }
        }
    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, News>): RemoteKey? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { news -> newsDao.remoteKeysId(news.title) }
    }
}
