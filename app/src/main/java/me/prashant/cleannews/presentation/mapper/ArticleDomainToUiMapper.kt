package me.prashant.cleannews.presentation.mapper

import me.prashant.cleannews.domain.model.ArticleDomainModel
import me.prashant.cleannews.presentation.model.ArticleUiModel
import me.prashant.cleannews.util.core.Mapper
import javax.inject.Inject

class ArticleDomainToUiMapper
    @Inject
    constructor() : Mapper<ArticleDomainModel, ArticleUiModel> {
        override fun convert(from: ArticleDomainModel): ArticleUiModel =
            ArticleUiModel(
                title = from.title,
                author = from.author,
                description = from.description,
                url = from.url,
                imageUrl = from.urlToImage,
                publishedAt = from.publishedAt,
            )
    }
