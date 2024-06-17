package me.prashant.cleannews.domain.repository

import me.prashant.cleannews.domain.model.ArticleDomainModel

interface NewsRepository {
    suspend fun getNews(): List<ArticleDomainModel>
}