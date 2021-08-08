package mx.malain.movieapp.datasource

import mx.malain.movieapp.data.AppDatabase
import mx.malain.movieapp.models.movie_detail.MovieDetail
import mx.malain.movieapp.models.videos.Video
import javax.inject.Inject

class MovieDetailLocalDataSource @Inject constructor(
    private val database: AppDatabase
) {
    suspend fun getMovieDetail(id: Long): MovieDetail {
        val moviesDetailDao = database.movieDetailDao()
        val movieDetail = moviesDetailDao.getMovieDetail(id)

        return movieDetail
    }

    suspend fun getMovieVideos(id: Long): List<Video> {
        val movieVideos = database.videosDao()
        val videos = movieVideos.getVideosForMovie(id)
        return videos
    }

    suspend fun saveMovieDetail(movieDetail: MovieDetail) {
        val movieDetailDao = database.movieDetailDao()
        movieDetailDao.insertMovieDetail(movieDetail)
    }

    suspend fun saveMovieVideos(videos: List<Video>) {
        val movieVideos = database.videosDao()
        videos.forEach {
            movieVideos.insertVideo(it)
        }
    }
}