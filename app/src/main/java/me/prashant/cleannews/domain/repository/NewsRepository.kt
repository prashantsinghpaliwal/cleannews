package me.prashant.cleannews.domain.repository

import kotlinx.coroutines.flow.Flow
import me.prashant.cleannews.domain.model.NewsDomainModel
import me.prashant.cleannews.util.core.Resource

interface NewsRepository {
    suspend fun getNews(page: Int): Flow<Resource<NewsDomainModel>>
}
