package mx.malain.movieapp.networking

import mx.malain.movieapp.models.MoviesListResponse
import mx.malain.movieapp.models.movie_detail.MovieDetail
import mx.malain.movieapp.models.videos.VideosResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesService {
    @GET("3/movie/popular")
    fun getMovies(
        @Query("api_key", encoded = true) apiKey: String,
        @Query("page") page: Int
    ): Call<MoviesListResponse>

    @GET("3/movie/top_rated")
    fun getTopRated(
        @Query("api_key", encoded = true) apiKey: String,
        @Query("page") page: Int
    ): Call<MoviesListResponse>

    @GET("3/movie/{movie_id}")
    fun getMovieDetail(
        @Path("movie_id", encoded = true) movieId: Long,
        @Query("api_key", encoded = true) apiKey: String
    ): Call<MovieDetail>

    @GET("3/movie/{movie_id}/videos")
    fun getMovieVides(
        @Path("movie_id", encoded = true) movieId: Long,
        @Query("api_key", encoded = true) apiKey: String
    ): Call<VideosResponse>

    @GET("3/search/movie")
    fun search(
        @Query("api_key", encoded = true) apiKey: String,
        @Query("page", encoded = true) page: Int,
        @Query("query", encoded = true) query: String
    ): Call<MoviesListResponse>
}