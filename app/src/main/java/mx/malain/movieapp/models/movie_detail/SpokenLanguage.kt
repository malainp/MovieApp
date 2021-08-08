package mx.malain.movieapp.models.movie_detail


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "spoken_languages")
data class SpokenLanguage(
    @SerializedName("english_name")
    val englishName: String,
    @PrimaryKey
    @SerializedName("iso_639_1")
    val iso6391: String,
    @SerializedName("name")
    val name: String
)