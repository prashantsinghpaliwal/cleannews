package me.prashant.cleannews.data.remote.model

// DTO - Data Transfer Object

data class NewsResponseDto(
    val status: String,
    val totalResults: Int,
    val articles: List<ArticleDto>,
)

data class ArticleDto(
    val source: SourceDto,
    val author: String?,
    val title: String,
    val description: String?,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String,
    val content: String?,
)

data class SourceDto(
    val id: String?,
    val name: String,
)
