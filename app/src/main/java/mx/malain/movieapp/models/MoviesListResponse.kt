package mx.malain.movieapp.models


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import androidx.room.Entity

@Keep
data class MoviesListResponse(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<Movie>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)