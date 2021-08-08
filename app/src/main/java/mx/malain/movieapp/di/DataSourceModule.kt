package mx.malain.movieapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import mx.malain.movieapp.data.AppDatabase
import mx.malain.movieapp.datasource.*
import mx.malain.movieapp.networking.MoviesService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {
    @Provides
    @Singleton
    fun provideMovieRemoteDataSource(
        moviesService: MoviesService,
        apiKey: String
    ): MoviesRemoteDataSource {
        return MoviesRemoteDataSource(moviesService, apiKey)
    }

    @Provides
    @Singleton
    fun provideMovieLocalDataSource(
        appDatabase: AppDatabase
    ): MoviesLocalDataSource {
        return MoviesLocalDataSource(appDatabase)
    }

    @Provides
    @Singleton
    fun provideMovieDetailRemoteDataSource(
        moviesService: MoviesService,
        apiKey: String
    ): MovieDetailRemoteDataSource {
        return MovieDetailRemoteDataSource(moviesService, apiKey)
    }

    @Provides
    @Singleton
    fun provideMovieDetailLocalDataSource(
        appDatabase: AppDatabase
    ): MovieDetailLocalDataSource {
        return MovieDetailLocalDataSource(appDatabase)
    }

    @Provides
    @Singleton
    fun provideSearchRemoteDataSource(
        moviesService: MoviesService,
        apiKey: String
    ): SearchRemoteDataSource {
        return SearchRemoteDataSource(moviesService, apiKey)
    }
}