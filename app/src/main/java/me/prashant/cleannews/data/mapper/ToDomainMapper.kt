package me.prashant.cleannews.data.mapper

import me.prashant.cleannews.data.remote.model.ArticleDto
import me.prashant.cleannews.data.remote.model.NewsResponseDto
import me.prashant.cleannews.data.remote.model.SourceDto
import me.prashant.cleannews.domain.model.ArticleDomainModel
import me.prashant.cleannews.domain.model.NewsDomainModel
import me.prashant.cleannews.domain.model.SourceDomainModel

fun NewsResponseDto.toDomain(): NewsDomainModel =
    NewsDomainModel(
        status = this.status,
        totalResults = this.totalResults,
        articles = this.articles.map { it.toDomain() },
    )

fun ArticleDto.toDomain(): ArticleDomainModel =
    ArticleDomainModel(
        source = this.source.toDomain(),
        author = this.author,
        title = this.title,
        description = this.description,
        url = this.url,
        urlToImage = this.urlToImage,
        publishedAt = this.publishedAt,
        content = this.content,
    )

fun SourceDto.toDomain(): SourceDomainModel =
    SourceDomainModel(
        id = this.id,
        name = this.name,
    )
