package com.tyryshkin.newsapp.di

import android.app.Application
import androidx.paging.ExperimentalPagingApi
import com.tyryshkin.newsapp.ui.news.NewsActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    @ExperimentalPagingApi
    fun inject(newsActivity: NewsActivity)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun create(): AppComponent
    }
}