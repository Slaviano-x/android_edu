package com.tyryshkin.newsapp.data.network

import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ApiUtilities {

    companion object {
        private const val BASE_URL = "https://newsapi.org/v2/"
        const val API_KEY = "ca07fdc7b9814be09f843989b820c395"

        private var retrofit: Retrofit? = null

        fun getNewsApiService(): NewsApiService {

            if (retrofit == null) {
                retrofit = Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .build()
            }
            return retrofit!!.create(NewsApiService::class.java)
        }
    }
}