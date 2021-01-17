package com.example.retrofit_news_app.data

import androidx.paging.PagingSource
import com.example.retrofit_news_app.api.NewsApi
import retrofit2.HttpException
import java.io.IOException

private const val NEWS_STARTING_PAGE_INDEX = 1

class NewsPagingSource(
    private val newsApi: NewsApi,
    private val query: String
) : PagingSource<Int, ArticlesModel>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticlesModel> {
        val position = params.key ?: NEWS_STARTING_PAGE_INDEX

        return try {
            val response = newsApi.searchNews(query, position, params.loadSize)
            val posts = response.articles

            LoadResult.Page(
                data = posts,
                prevKey = if (position == NEWS_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (posts.isEmpty()) null else position + 1
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }
}