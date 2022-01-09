package com.tyryshkin.newsapp.models.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class News(
    @PrimaryKey(autoGenerate = false) var title: String,
    var description: String?,
    var urlToImage: String?,
    var dataPublishedAt: String,
    var url: String?
)