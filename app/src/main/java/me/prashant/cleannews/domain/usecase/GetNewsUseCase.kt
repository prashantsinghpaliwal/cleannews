package me.prashant.cleannews.domain.usecase

import kotlinx.coroutines.flow.Flow
import me.prashant.cleannews.domain.model.NewsDomainModel
import me.prashant.cleannews.domain.repository.NewsRepository
import me.prashant.cleannews.util.core.Resource
import javax.inject.Inject

class GetNewsUseCase
    @Inject
    constructor(
        private val newsRepository: NewsRepository,
    ) {
        suspend operator fun invoke(page: Int): Flow<Resource<NewsDomainModel>> = newsRepository.getNews(page)
    }
