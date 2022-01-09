package com.tyryshkin.newsapp.data.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tyryshkin.newsapp.models.entities.News

@Dao
interface NewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNews(news: List<News>)

    @Query("SELECT * FROM news")
    fun getNews(): PagingSource<Int, News>

    @Query("DELETE FROM news")
    suspend fun deleteNews()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllRemoteKeys(remoteKey: List<RemoteKey>)

    @Query("SELECT * FROM remote_keys WHERE id = :id")
    suspend fun remoteKeysId(id: String): RemoteKey?

    @Query("DELETE FROM remote_keys")
    suspend fun deleteAllRemoteKeys()
}