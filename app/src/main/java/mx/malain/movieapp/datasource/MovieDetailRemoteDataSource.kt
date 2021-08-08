package mx.malain.movieapp.datasource

import mx.malain.movieapp.models.movie_detail.MovieDetail
import mx.malain.movieapp.models.videos.Video
import mx.malain.movieapp.networking.MoviesService
import javax.inject.Inject

class MovieDetailRemoteDataSource @Inject constructor(
    private val moviesService: MoviesService,
    private val apiKey: String
) {
    suspend fun getMovieDetail(id: Long): MovieDetail? {
        val result = moviesService.getMovieDetail(id, apiKey).execute()
        result.body()?.let {
            return@getMovieDetail it
        }
        return null
    }

    suspend fun getMovieVideos(id: Long): List<Video> {
        val result = moviesService.getMovieVides(id, apiKey).execute()
        result.body()?.let {
            it.results.forEach { it.movieId = id }
            return@getMovieVideos it.results
        }
        return listOf()
    }
}