package mx.malain.movieapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import mx.malain.movieapp.datasource.*
import mx.malain.movieapp.networking.MoviesService
import mx.malain.movieapp.repositories.MovieDetailRepo
import mx.malain.movieapp.repositories.MoviesRepo
import mx.malain.movieapp.repositories.SearchRepo
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepoModule {
    @Provides
    @Singleton
    fun provideMoviesRepository(
        remoteDataSource: MoviesRemoteDataSource,
        localDataSource: MoviesLocalDataSource
    ): MoviesRepo {
        return MoviesRepo(remoteDataSource, localDataSource)
    }

    @Provides
    @Singleton
    fun provideMovieDetailRepository(
        remoteDataSource: MovieDetailRemoteDataSource,
        localDataSource: MovieDetailLocalDataSource
    ): MovieDetailRepo {
        return MovieDetailRepo(remoteDataSource, localDataSource)
    }

    @Provides
    @Singleton
    fun provideSearchRepository(
        remoteDataSource: SearchRemoteDataSource
    ): SearchRepo {
        return SearchRepo(remoteDataSource)
    }
}