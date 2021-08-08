package mx.malain.movieapp.datasource

import mx.malain.movieapp.data.AppDatabase
import mx.malain.movieapp.models.Movie
import javax.inject.Inject

open class MoviesLocalDataSource @Inject constructor(
    private val database: AppDatabase
) {
    open suspend fun getMovies(page: Int = 1): List<Movie> {
        val moviesDao = database.movieDao()
        val movies = moviesDao.getAll()
        val initIndex = if (page == 1) 0 else (page -1) * 20

        if (movies.isNotEmpty() && initIndex + 20 <= movies.size) {
            return movies.subList(initIndex, initIndex + 20)
        }
        return listOf()
    }

    open suspend fun insertMovies(list: List<Movie>) {
        val moviesDao = database.movieDao()
        moviesDao.insertAll(list)
    }
}