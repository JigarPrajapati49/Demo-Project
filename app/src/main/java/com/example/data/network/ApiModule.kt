package com.example.data.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ApiModule {

    @Provides
    @Singleton
    fun provideApiInterFace(networkConnectionInterceptor: NetworkConnectionInterceptor): ApiInterface {
        return RetrofitClient.webService(networkConnectionInterceptor)
    }
}