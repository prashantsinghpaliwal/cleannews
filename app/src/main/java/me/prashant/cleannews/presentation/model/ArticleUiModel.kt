package me.prashant.cleannews.presentation.model

data class NewsUIModel(
    val totalResults: Int,
    val articles: List<ArticleUiModel>,
)

data class ArticleUiModel(
    val title: String,
    val author: String?,
    val description: String?,
    val url: String,
    val imageUrl: String?,
    val publishedAt: String,
)
