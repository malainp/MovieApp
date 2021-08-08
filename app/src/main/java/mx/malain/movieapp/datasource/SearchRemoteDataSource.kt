package mx.malain.movieapp.datasource

import mx.malain.movieapp.models.Movie
import mx.malain.movieapp.networking.MoviesService
import javax.inject.Inject

class SearchRemoteDataSource @Inject constructor(
    private val moviesService: MoviesService,
    private val apiKey: String
) {
    suspend fun search(query: String, page: Int): List<Movie> {
        val result = moviesService.search(apiKey, page, query).execute()
        result.body()?.let {
            return@search it.results
        }
        return listOf()
    }
}