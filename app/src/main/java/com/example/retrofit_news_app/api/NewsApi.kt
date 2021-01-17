package com.example.retrofit_news_app.api

import com.example.retrofit_news_app.BuildConfig
import com.example.retrofit_news_app.data.ArticlesModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    companion object {
        const val BASE_URL = "http://newsapi.org/v2/"
        const val CLIENT_ID = BuildConfig.NEWS_ACCESS_KEY
    }


    @GET("everything")
    suspend fun searchNews(
        @Query("q") q: String,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int,
        @Query("apiKey") apiKey: String = CLIENT_ID
    ): ArticlesModelResponse //need to use this method in future

    @GET("everything")
    fun getPosts(
        @Query("q") q: String,
        @Query("sortBy") sortBy: String,
        @Query("apiKey") apiKey: String = CLIENT_ID
    ): Call<ArticlesModel>
}