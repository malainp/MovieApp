package mx.malain.movieapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mx.malain.movieapp.models.movie_detail.MovieDetail

@Dao
interface MovieDetailDao {
    @Query("SELECT * FROM movie_details WHERE id = :id")
    fun getMovieDetail(id: Long) : MovieDetail

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovieDetail(movie: MovieDetail): Long

}