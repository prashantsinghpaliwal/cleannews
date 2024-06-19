package me.prashant.cleannews.presentation.mapper

import me.prashant.cleannews.domain.model.NewsDomainModel
import me.prashant.cleannews.presentation.model.NewsUIModel
import me.prashant.cleannews.util.core.Mapper
import javax.inject.Inject

class NewsDomainToUiMapper
    @Inject
    constructor(
        private val articleDomainToUiMapper: ArticleDomainToUiMapper,
    ) : Mapper<NewsDomainModel, NewsUIModel> {
        override fun convert(from: NewsDomainModel): NewsUIModel =
            NewsUIModel(
                totalResults = from.totalResults,
                articles = from.articles.map { articleDomainToUiMapper.convert(it) },
            )
    }
