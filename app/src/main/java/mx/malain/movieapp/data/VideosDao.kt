package mx.malain.movieapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mx.malain.movieapp.models.videos.Video

@Dao
interface VideosDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVideo(video: Video): Long

    @Query("SELECT * FROM videos WHERE movieId = :id")
    fun getVideosForMovie(id: Long): List<Video>
}