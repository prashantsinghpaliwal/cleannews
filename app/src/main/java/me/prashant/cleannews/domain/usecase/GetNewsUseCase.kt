package me.prashant.cleannews.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import me.prashant.cleannews.domain.model.NewsDomainModel
import me.prashant.cleannews.domain.repository.NewsRepository
import me.prashant.cleannews.util.core.Resource
import javax.inject.Inject

class GetNewsUseCase
    @Inject
    constructor(
        private val newsRepository: NewsRepository,
    ) {
        operator fun invoke(page: Int): Flow<Resource<NewsDomainModel>> =
            flow {
                try {
                    emit(Resource.Loading)
                    val articles = newsRepository.getNews(page)
                    emit(Resource.Success(articles))
                } catch (e: Exception) {
                    emit(Resource.Error(e))
                }
            }
    }
