package mx.malain.movieapp.repositories

import mx.malain.movieapp.datasource.MovieDetailLocalDataSource
import mx.malain.movieapp.datasource.MovieDetailRemoteDataSource
import mx.malain.movieapp.models.movie_detail.MovieDetail
import mx.malain.movieapp.models.videos.Video
import mx.malain.movieapp.models.videos.VideosResponse
import mx.malain.movieapp.networking.MoviesService
import javax.inject.Inject

class MovieDetailRepo @Inject constructor(
    private val remoteDataSource: MovieDetailRemoteDataSource,
    private val localDataSource: MovieDetailLocalDataSource
) {

    suspend fun getMovieDetail(id: Long, isConnectionAvailable: Boolean): MovieDetail? {
        if (isConnectionAvailable) {
            val movieDetail = remoteDataSource.getMovieDetail(id)
            movieDetail?.let {
                localDataSource.saveMovieDetail(it)
                return@getMovieDetail it
            }
            return null
        } else {
            return localDataSource.getMovieDetail(id)
        }

    }

    suspend fun getMovieVideos(id: Long, isConnectionAvailable: Boolean): List<Video> {
        if (isConnectionAvailable) {
            val videos = remoteDataSource.getMovieVideos(id)
            localDataSource.saveMovieVideos(videos)
            return videos
        } else {
            return localDataSource.getMovieVideos(id)
        }
    }



}