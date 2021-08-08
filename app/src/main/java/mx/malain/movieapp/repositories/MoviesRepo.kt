package mx.malain.movieapp.repositories

import mx.malain.movieapp.datasource.MoviesLocalDataSource
import mx.malain.movieapp.datasource.MoviesRemoteDataSource
import mx.malain.movieapp.models.Movie
import mx.malain.movieapp.networking.MoviesService
import retrofit2.await
import javax.inject.Inject

class MoviesRepo @Inject constructor(
    private val remoteDataSource: MoviesRemoteDataSource,
    private val localDataSource: MoviesLocalDataSource
) {
    suspend fun getMovies(page: Int = 1, isConnectionAvailable: Boolean): List<Movie> {
        if (isConnectionAvailable) {
            val movies = remoteDataSource.getMovies(page)
            localDataSource.insertMovies(movies)
            return movies
        } else
            return localDataSource.getMovies(page)
    }

    suspend fun getTopRatedMovies(page: Int = 1, isConnectionAvailable: Boolean): List<Movie> {
        if (isConnectionAvailable) {
            val movies = remoteDataSource.getTopRatedMovies(page)
            localDataSource.insertMovies(movies)
            return movies
        } else
            return localDataSource.getMovies(page)
    }
}