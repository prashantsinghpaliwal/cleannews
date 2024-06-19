package me.prashant.cleannews.data.repository

import me.prashant.cleannews.data.mapper.toDomain
import me.prashant.cleannews.data.remote.api.NewsApiService
import me.prashant.cleannews.domain.model.NewsDomainModel
import me.prashant.cleannews.domain.repository.NewsRepository
import javax.inject.Inject

class NewsRepositoryImpl
    @Inject
    constructor(
        private val newsApiService: NewsApiService,
    ) : NewsRepository {
        override suspend fun getNews(page: Int): NewsDomainModel {
            val response =
                newsApiService.getTopHeadlines(
                    apiKey = "541743e949524b4c9631d0fa0686e080",
                    country = "in",
                    page = page,
                )

            return response.toDomain()
        }
    }
