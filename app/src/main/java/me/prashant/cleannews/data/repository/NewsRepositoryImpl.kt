package me.prashant.cleannews.data.repository

import me.prashant.cleannews.data.mapper.toDomain
import me.prashant.cleannews.data.remote.api.NewsApiService
import me.prashant.cleannews.domain.model.ArticleDomainModel
import me.prashant.cleannews.domain.repository.NewsRepository
import javax.inject.Inject

class NewsRepositoryImpl
    @Inject
    constructor(
        private val newsApiService: NewsApiService,
    ) : NewsRepository {
        override suspend fun getNews(): List<ArticleDomainModel> {
            val response =
                newsApiService.getTopHeadlines(
                    apiKey = "541743e949524b4c9631d0fa0686e080",
                    country = "in",
                )
            return response.articles.map { it.toDomain() }
        }
    }
