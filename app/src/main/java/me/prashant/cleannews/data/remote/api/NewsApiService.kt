package me.prashant.cleannews.data.remote.api

import me.prashant.cleannews.data.remote.model.NewsResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String,
        @Query("apiKey") apiKey: String,
    ): NewsResponseDto
}