package mx.malain.movieapp.models.movie_detail


import androidx.room.*
import com.google.gson.annotations.SerializedName
import mx.malain.movieapp.utils.GenresDataConverter
import mx.malain.movieapp.utils.ProductionCompanyDataConverter

@Entity(tableName = "movie_details")
data class MovieDetail(
    @SerializedName("adult")
    var adult: Boolean,
    @SerializedName("backdrop_path")
    var backdropPath: String?,
    @SerializedName("budget")
    var budget: Int,

    @TypeConverters(GenresDataConverter::class)
    @SerializedName("genres")
    var genres: List<Genre>,
    @SerializedName("homepage")
    var homepage: String?,
    @PrimaryKey
    @SerializedName("id")
    var id: Long,
    @SerializedName("imdb_id")
    var imdbId: String?,
    @SerializedName("original_language")
    var originalLanguage: String,
    @SerializedName("original_title")
    var originalTitle: String,
    @SerializedName("overview")
    var overview: String,
    @SerializedName("popularity")
    var popularity: Double,
    @SerializedName("poster_path")
    var posterPath: String?,
    @TypeConverters(ProductionCompanyDataConverter::class)
    @SerializedName("production_companies")
    var productionCompanies: List<ProductionCompany>,
    @Ignore
    @SerializedName("production_countries")
    var productionCountries: List<ProductionCountry>,
    @SerializedName("release_date")
    var releaseDate: String,
    @SerializedName("revenue")
    var revenue: Long,
    @SerializedName("runtime")
    var runtime: Int?,
    @Ignore
    @SerializedName("spoken_languages")
    var spokenLanguages: List<SpokenLanguage>,
    @SerializedName("status")
    var status: String,
    @SerializedName("tagline")
    var tagline: String?,
    @SerializedName("title")
    var title: String,
    @SerializedName("video")
    var video: Boolean,
    @SerializedName("vote_average")
    var voteAverage: Double,
    @SerializedName("vote_count")
    var voteCount: Int
) {
    constructor(): this(false, null, 0, listOf(), null, 0, null, "", "", "", 0.0, null, listOf(), listOf(),"", 0,null,
        listOf(),"",null, "",false,0.0,0)
}