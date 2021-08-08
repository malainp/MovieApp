package mx.malain.movieapp.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import mx.malain.movieapp.models.movie_detail.ProductionCompany
import java.lang.reflect.Type
import java.util.*

class ProductionCompanyDataConverter {

    var gson = Gson()

    @TypeConverter
    fun stringProductionCompanyList(data: String?): List<ProductionCompany> {
        if (data == null) {
            return Collections.emptyList()
        }
        val listType: Type = object : TypeToken<List<ProductionCompany?>?>() {}.type
        return gson.fromJson<List<ProductionCompany>>(data, listType)
    }

    @TypeConverter
    fun productionCompanyListToString(someObjects: List<ProductionCompany?>?): String {
        return gson.toJson(someObjects)
    }

}