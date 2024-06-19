package me.prashant.cleannews.domain.repository

import me.prashant.cleannews.domain.model.NewsDomainModel

interface NewsRepository {
    suspend fun getNews(page: Int): NewsDomainModel
}
