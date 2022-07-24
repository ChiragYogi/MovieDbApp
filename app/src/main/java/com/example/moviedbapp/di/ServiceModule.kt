package com.example.moviedbapp.di

import com.example.moviedbapp.network.TmdbNetworkService
import com.example.moviedbapp.repo.MainTmdbRepository
import com.example.moviedbapp.repo.TmdbRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Singleton
    @Provides
    fun provideTmdbService(retrofit: Retrofit): TmdbNetworkService {
        return retrofit.create(TmdbNetworkService::class.java)
    }

    @Singleton
    @Provides
    fun provideTmdbResponse(tmbdMainTmdbRepository: MainTmdbRepository): TmdbRepository {
        return tmbdMainTmdbRepository
    }
}