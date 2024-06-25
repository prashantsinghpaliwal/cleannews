package me.prashant.cleannews.data.repository

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import me.prashant.cleannews.data.local.db.NewsDao
import me.prashant.cleannews.data.mapper.toDomain
import me.prashant.cleannews.data.mapper.toEntity
import me.prashant.cleannews.data.remote.api.NewsApiService
import me.prashant.cleannews.domain.model.NewsDomainModel
import me.prashant.cleannews.domain.repository.NewsRepository
import me.prashant.cleannews.util.core.Resource
import javax.inject.Inject

class NewsRepositoryImpl
    @Inject
    constructor(
        private val newsApiService: NewsApiService,
        private val newsDao: NewsDao,
    ) : NewsRepository {
        override suspend fun getNews(page: Int): Flow<Resource<NewsDomainModel>> =
            flow {
                emit(Resource.Loading(true))
                val localData = newsDao.getAllNews().firstOrNull()

                if (!localData.isNullOrEmpty()) {
                    val domainArticles = localData.map { it.toDomain() }
                    emit(
                        Resource.Success(
                            NewsDomainModel(
                                status = "ok",
                                totalResults = domainArticles.size,
                                articles = domainArticles,
                            ),
                        ),
                    )
                }

                try {
                    val response =
                        newsApiService.getTopHeadlines(
                            apiKey = "541743e949524b4c9631d0fa0686e080",
                            country = "in",
                            page = page,
                        )
                    val articles = response.articles.map { it.toEntity() }
                    newsDao.clearAll()
                    newsDao.insertAll(articles)
                    emit(Resource.Success(response.toDomain()))
                } catch (e: Exception) {
                    emit(Resource.Error(e))
                }
            }

        override suspend fun getNewsBySearchQuery(
            query: String,
            page: Int,
        ): Flow<Resource<NewsDomainModel>> =
            flow {
                emit(Resource.Loading(true))

                try {
                    val response =
                        newsApiService.searchNews(
                            apiKey = "541743e949524b4c9631d0fa0686e080",
                            query = query,
                        )
                    emit(Resource.Success(response.toDomain()))
                } catch (e: Exception) {
                    Log.v("ErrorLog", "$query | e: $e, ${e.message}, ${e.localizedMessage}, ")
                    emit(Resource.Error(e))
                }
            }
    }
