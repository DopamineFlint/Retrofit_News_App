package com.example.retrofit_news_app.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.retrofit_news_app.api.NewsApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton //??? constructor
class NewsRepository @Inject constructor(private val newsApi: NewsApi) {
    fun getSearchResults(query: String) =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { NewsPagingSource(newsApi, query) }
        ).liveData
}