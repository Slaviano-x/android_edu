package com.tyryshkin.newsapp.models.response

import com.google.gson.annotations.SerializedName

class ArticleNetwork(
    @SerializedName("author") val author: String?,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String?,
    @SerializedName("url") val url: String?,
    @SerializedName("urlToImage") val urlToImage: String?,
    @SerializedName("publishedAt") val dataPublishedAt: String,
    @SerializedName("content") val content: String?
)