package me.prashant.cleannews.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.prashant.cleannews.data.remote.api.NewsApiService
import me.prashant.cleannews.data.repository.NewsRepositoryImpl
import me.prashant.cleannews.domain.repository.NewsRepository
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun provideNewsApiService(): NewsApiService {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client =
            okhttp3.OkHttpClient
                .Builder()
                .addInterceptor(interceptor)
                .build()

        return Retrofit
            .Builder()
            .baseUrl("https://newsapi.org/v2/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideNewsRepository(newsApiService: NewsApiService): NewsRepository = NewsRepositoryImpl(newsApiService)
}
