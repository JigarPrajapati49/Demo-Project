package com.example.ktor.data.network

import com.example.ktor.CollectionsService
import com.example.ktor.CollectionsServiceImpl
import com.example.ktor.MoviesRepositoryImpl
import com.example.ktor.data.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    fun getHttpClient(httpClient: KtorClient): HttpClient = httpClient.getHttpClient()

    @Provides
    fun getMoviesRepository(repository: MoviesRepositoryImpl): ApiInterfaceKtor = repository

    @Provides
    fun getRepository(repo: CollectionsServiceImpl): CollectionsService = repo


    @Provides
    @Singleton
    fun provideApiService(httpClient: HttpClient): ApiService {
        return ApiService(httpClient)
    }


}