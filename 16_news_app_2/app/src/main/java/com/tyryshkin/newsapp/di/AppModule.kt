package com.tyryshkin.newsapp.di

import android.content.Context
import com.tyryshkin.newsapp.data.room.NewsDao
import com.tyryshkin.newsapp.data.room.NewsDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {
    @Singleton
    @Provides
    fun provideNewsDatabase(context: Context): NewsDatabase {
        return NewsDatabase.getInstance(context)
    }

    @Singleton
    @Provides
    fun provideNewsDao(newsDatabase: NewsDatabase): NewsDao {
        return newsDatabase.getNewsDao()
    }
}