package mx.malain.movieapp.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import mx.malain.movieapp.R
import mx.malain.movieapp.models.Movie
import org.w3c.dom.Text

class MoviesAdapter(context: Context): GenericRecyclerViewAdapter<Movie>(context, null) {
    override fun createView(context: Context, viewGroup: ViewGroup, viewType: Int): View {
        return LayoutInflater.from(context).inflate(viewType, viewGroup, false)
    }

    override fun bindView(item: Movie?, position: Int, viewHolder: ViewHolder) {
        item?.let { movie ->
            val movieImage = viewHolder.getView<ImageView>(R.id.movie_image)
            val movieTitle = viewHolder.getView<TextView>(R.id.movie_title)
            val movieSinopsys = viewHolder.getView<TextView>(R.id.movie_sinopsys)

            with(movie){
                val url = context.getString(R.string.url_movie_image, backdropPath)
                Glide.with(context)
                    .load(url/*"https://image.tmdb.org/t/p/original${backdropPath}"*/)
                    .centerCrop()
                    .into(movieImage)
                movieTitle.text = title
                movieSinopsys.text = movie.overview
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.movie_layout
    }
}