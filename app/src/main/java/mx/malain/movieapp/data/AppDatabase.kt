package mx.malain.movieapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import mx.malain.movieapp.models.Movie
import mx.malain.movieapp.models.movie_detail.MovieDetail
import mx.malain.movieapp.models.videos.Video
import mx.malain.movieapp.utils.GenresDataConverter
import mx.malain.movieapp.utils.ProductionCompanyDataConverter

@Database(entities = arrayOf(Movie::class, MovieDetail::class, Video::class), version = 1)
@TypeConverters(GenresDataConverter::class, ProductionCompanyDataConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao
    abstract fun movieDetailDao(): MovieDetailDao
    abstract fun videosDao(): VideosDao
}