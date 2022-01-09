package com.tyryshkin.newsapp.models.response

class NewsResponse (private val status: String,
                    private val totalResults: Int,
                    private val articles: List<ArticleNetwork>){

    fun getStatus() : String {
        return status
    }

    fun getTotalResults(): Int {
        return totalResults
    }

    fun getArticles() : List<ArticleNetwork> {
        return articles
    }

}