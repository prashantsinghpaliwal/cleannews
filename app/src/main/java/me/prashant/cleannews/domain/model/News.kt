package me.prashant.cleannews.domain.model

data class NewsDomainModel(
    val status: String,
    val totalResults: Int,
    val articles: List<ArticleDomainModel>,
)

data class ArticleDomainModel(
    val source: SourceDomainModel,
    val author: String?,
    val title: String,
    val description: String?,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String,
    val content: String?,
)

data class SourceDomainModel(
    val id: String?,
    val name: String,
)
