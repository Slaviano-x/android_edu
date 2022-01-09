package com.tyryshkin.newsapp.models.mappers

import com.tyryshkin.newsapp.models.entities.News
import com.tyryshkin.newsapp.models.response.ArticleNetwork

class NewsMapper {

    fun newsDataToEntity(articleNetwork: ArticleNetwork): News {
        val urlToImage = articleNetwork.urlToImage
        val title = articleNetwork.title
        val description = articleNetwork.description
        val dataPublishedAt = articleNetwork.dataPublishedAt
        val url = articleNetwork.url
        return News(title, description, urlToImage, dataPublishedAt, url)
    }
}