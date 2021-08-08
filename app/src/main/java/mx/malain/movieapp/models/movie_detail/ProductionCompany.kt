package mx.malain.movieapp.models.movie_detail


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "production_companies")
data class ProductionCompany(
    @PrimaryKey
    @SerializedName("id")
    val id: Long,
    @SerializedName("logo_path")
    val logoPath: String?,
    @SerializedName("name")
    val name: String,
    @SerializedName("origin_country")
    val originCountry: String
)