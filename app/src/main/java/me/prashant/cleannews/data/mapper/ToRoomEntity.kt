package me.prashant.cleannews.data.mapper

import me.prashant.cleannews.data.local.model.NewsEntity
import me.prashant.cleannews.data.remote.model.ArticleDto
import java.util.UUID

fun ArticleDto.toEntity(): NewsEntity =
    NewsEntity(
        id = UUID.randomUUID().toString(),
        title = this.title,
        description = this.description ?: "",
        url = this.url,
        urlToImage = this.urlToImage,
        publishedAt = this.publishedAt,
        sourceName = this.source.name,
        author = this.author ?: "",
        content = this.content ?: "",
    )
