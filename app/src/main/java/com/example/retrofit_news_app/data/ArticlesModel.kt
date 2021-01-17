package com.example.retrofit_news_app.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ArticlesModel(
    val source: SourceModel,
    val author: String,
    val title: String,
    val description: String,
    val url: String,
    val urlToImage: String,
    val publishedAt: String,
    val content: String
) : Parcelable {

    @Parcelize
    data class SourceModel(
        val id: String,
        val name: String
    ) : Parcelable
}