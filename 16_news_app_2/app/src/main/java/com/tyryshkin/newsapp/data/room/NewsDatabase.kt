package com.tyryshkin.newsapp.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tyryshkin.newsapp.models.entities.News

@Database(entities = [News::class, RemoteKey::class], version = 1)
abstract class NewsDatabase : RoomDatabase() {

    companion object {
        fun getInstance(context: Context): NewsDatabase {
            return Room.databaseBuilder(context, NewsDatabase::class.java, "name").build()
        }
    }

    abstract fun getNewsDao(): NewsDao
}