package mx.malain.movieapp

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.MenuItemCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter
import kotlinx.android.synthetic.main.activity_main.*
import mx.malain.movieapp.extensions.isConnectionAvailable
import mx.malain.movieapp.extensions.observe
import mx.malain.movieapp.models.Movie
import mx.malain.movieapp.ui.MovieDetailActivity
import mx.malain.movieapp.utils.GenericRecyclerViewAdapter
import mx.malain.movieapp.utils.MoviesAdapter
import mx.malain.movieapp.viewmodel.MovieDetailViewModel
import mx.malain.movieapp.viewmodel.MoviesListViewModel


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MoviesListViewModel by viewModels()
    private val detailViewModel: MovieDetailViewModel by viewModels()
    lateinit var moviesAdapter: MoviesAdapter
    var pastVisibleItems = 0
    var visibleItemCount = 0
    lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupView()
        observe(viewModel.movie, ::observeMovies)
        viewModel.getMovies(isConnectionAvailable())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        val searchViewItem = menu?.findItem(R.id.app_bar_search)
        val searchView: SearchView = MenuItemCompat.getActionView(searchViewItem) as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                /*   if(list.contains(query)){
                    adapter.getFilter().filter(query);
                }else{
                    Toast.makeText(MainActivity.this, "No Match found",Toast.LENGTH_LONG).show();
                }*/return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                //adapter.getFilter().filter(newText)
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }


    private fun setupView() {
        moviesAdapter = MoviesAdapter(this)
        layoutManager = LinearLayoutManager(this)
        movies_recycler.layoutManager = layoutManager

        /*val animation = android.view.animation.AnimationUtils.loadLayoutAnimation(  this, R.anim.layout_animation_fall_down)
        movies_recycler.layoutAnimation = animation*/

        moviesAdapter.setClickListener(
            object : GenericRecyclerViewAdapter.OnViewHolderClickListener<Movie> {
                override fun onClick(view: View, position: Int, item: Movie?) {
                    item?.let {
                        val intent = Intent(this@MainActivity, MovieDetailActivity::class.java)
                        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                            this@MainActivity,
                            view.findViewById(R.id.movie_image),
                            "mimage"
                        )
                        intent.putExtra(MovieDetailActivity.MOVIE, it)
                        startActivity(intent, options.toBundle())
                    }
                }
            }
        )
        movies_recycler.adapter = SlideInBottomAnimationAdapter(moviesAdapter).apply {
            // Change the durations.
            setDuration(1000)
            // Change the interpolator.
            setInterpolator(OvershootInterpolator())
            // Disable the first scroll mode.
            setFirstOnly(false)
        }
        movies_recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    visibleItemCount = layoutManager.childCount
                    pastVisibleItems = layoutManager.findFirstVisibleItemPosition()
                    if ((visibleItemCount + pastVisibleItems) >= moviesAdapter.itemCount) {
                        viewModel.getMovies(isConnectionAvailable())
                    }
                }
            }
        })
    }

    private fun observeMovies(list: List<Movie>) {
        //list.forEach { moviesAdapter.addAt(moviesAdapter.itemCount, it) }
        moviesAdapter.addAll(list)
        val internet = isConnectionAvailable()
        if (internet) {
            detailViewModel.saveMovieDetails(list, internet)
        }
    }
}