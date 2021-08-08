package mx.malain.movieapp.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_movie_detail.*
import kotlinx.android.synthetic.main.content_movie_detail.*
import mx.malain.movieapp.R
import mx.malain.movieapp.extensions.observe
import mx.malain.movieapp.models.Movie
import mx.malain.movieapp.models.movie_detail.MovieDetail
import mx.malain.movieapp.models.videos.Video
import mx.malain.movieapp.utils.MovieVideoAdapter
import mx.malain.movieapp.utils.ProductionCompaniesAdapter
import mx.malain.movieapp.viewmodel.MovieDetailViewModel
import android.view.WindowManager

import android.os.Build
import android.view.Window
import androidx.core.content.ContextCompat
import mx.malain.movieapp.extensions.isConnectionAvailable
import mx.malain.movieapp.utils.GenericRecyclerViewAdapter


@AndroidEntryPoint
class MovieDetailActivity : AppCompatActivity() {

    private val viewModel: MovieDetailViewModel by viewModels()

    lateinit var companiesAdapter: ProductionCompaniesAdapter
    lateinit var videosAdapter: MovieVideoAdapter

    var movie: Movie? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, R.color.purple_700)
        }
        setSupportActionBar(toolbar)

        observe(viewModel.movieDetail, ::observeMovieDetail)
        observe(viewModel.videos, ::observeMovieVideos)

        if (intent.hasExtra(MOVIE)) {
            movie = intent.getSerializableExtra(MOVIE) as Movie?
        }
        movie?.let {
            viewModel.getMovieDetail(it.id, isConnectionAvailable())
            viewModel.getMovieVideos(it.id, isConnectionAvailable())
            // Use prefetched image to make transition work
            val url = getString(R.string.url_movie_image, it.backdropPath)
            Glide.with(this)
                .load(url/*"https://image.tmdb.org/t/p/original${it.backdropPath}"*/)
                .into(movie_image)
            supportActionBar?.let { ab ->
                with(ab) {
                    title = it.title
                    setDisplayHomeAsUpEnabled(true)
                }
            }
        }
    }

    private fun observeMovieDetail(movie: MovieDetail) {

        val rating = getString(R.string.movie_detail_ratio, movie.releaseDate, movie.voteCount, movie.voteAverage, if (movie.adult) "🔞 Contains adult scenes" else "✅ Safe for childrens")
        movie_rating.text = rating // "🗓 Released at ${movie.releaseDate}\n🗳 ${movie.voteCount} votes\n⭐️ ${movie.voteAverage} average\n${if (movie.adult) "🔞 Contains adult scenes" else "✅ Safe for childrens"}"
        movie_resume.text = movie.overview

        var genres = ""
        movie.genres.forEach { g -> genres += g.name + "\n" }
        movie_genres.text = genres.trim('\n')

        companiesAdapter = ProductionCompaniesAdapter(this)
        companiesAdapter.addAll(movie.productionCompanies)
        if (movie.productionCompanies.isEmpty()) {
            production_companies_recycler.visibility = View.GONE
            produced_by.visibility = View.GONE
        } else {
            production_companies_recycler.visibility = View.VISIBLE
            produced_by.visibility = View.VISIBLE
        }
        var layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        production_companies_recycler.layoutManager = layoutManager
        production_companies_recycler.adapter = companiesAdapter

    }

    private fun observeMovieVideos(videos: List<Video>) {
        videosAdapter = MovieVideoAdapter(this)
        videosAdapter.setClickListener(object: GenericRecyclerViewAdapter.OnViewHolderClickListener<Video>{
            override fun onClick(view: View, position: Int, item: Video?) {
                item?.let {
                    val intent  = Intent(this@MovieDetailActivity, VideoActivity::class.java)
                    val url = getString(R.string.url_videos, it.key)
                    intent.putExtra(VideoActivity.VIDEO_URL, url/*"https://www.youtube.com/embed/${it.key}?autoplay=1&vq=small"*/)
                    startActivity(intent)
                }
            }
        })
        videosAdapter.addAll(videos)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        videos_recycler.layoutManager = layoutManager
        videos_recycler.adapter = videosAdapter
    }

    companion object {
        val MOVIE = "MOVIE"
    }
}