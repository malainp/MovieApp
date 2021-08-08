package mx.malain.movieapp.datasource

import mx.malain.movieapp.models.Movie
import mx.malain.movieapp.networking.MoviesService
import javax.inject.Inject

open class MoviesRemoteDataSource @Inject constructor(
    private val moviesService: MoviesService,
    private val apiKey: String
) {
    open suspend fun getMovies(page: Int = 1): List<Movie> {
        val result = moviesService.getMovies(apiKey, page).execute()
        result.body()?.let {
            return@getMovies it.results
        }
        return listOf()
    }

    open suspend fun getTopRatedMovies(page: Int = 1): List<Movie> {
        val result = moviesService.getMovies(apiKey, page).execute()
        result.body()?.let {
            return@getTopRatedMovies it.results
        }
        return listOf()
    }
}