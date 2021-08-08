package mx.malain.movieapp.repositories

import mx.malain.movieapp.datasource.SearchRemoteDataSource
import mx.malain.movieapp.models.Movie
import javax.inject.Inject

class SearchRepo @Inject constructor(
    private val remoteDataSource: SearchRemoteDataSource
) {
    suspend fun search(query: String, page: Int): List<Movie> {
        return remoteDataSource.search(query, page)
    }
}