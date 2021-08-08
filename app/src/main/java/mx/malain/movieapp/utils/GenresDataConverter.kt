package mx.malain.movieapp.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import mx.malain.movieapp.models.movie_detail.Genre
import mx.malain.movieapp.models.movie_detail.ProductionCompany
import java.lang.reflect.Type
import java.util.*

class GenresDataConverter {
    var gson = Gson()

    @TypeConverter
    fun stringToGenreList(data: String?): List<Genre> {
        if (data == null) {
            return Collections.emptyList()
        }
        val listType: Type = object : TypeToken<List<Genre?>?>() {}.type
        return gson.fromJson<List<Genre>>(data, listType)
    }

    @TypeConverter
    fun genreListToString(someObjects: List<Genre?>?): String {
        return gson.toJson(someObjects)
    }

}