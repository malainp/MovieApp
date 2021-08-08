package mx.malain.movieapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import androidx.activity.viewModels
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter
import kotlinx.android.synthetic.main.activity_main.*
import mx.malain.movieapp.R
import mx.malain.movieapp.extensions.isConnectionAvailable
import mx.malain.movieapp.extensions.observe
import mx.malain.movieapp.models.Movie
import mx.malain.movieapp.utils.GenericRecyclerViewAdapter
import mx.malain.movieapp.utils.MoviesAdapter
import mx.malain.movieapp.viewmodel.MovieDetailViewModel
import mx.malain.movieapp.viewmodel.MoviesListViewModel

@AndroidEntryPoint
class MoviesFragment : Fragment() {

    private val viewModel: MoviesListViewModel by viewModels()
    private val detailViewModel: MovieDetailViewModel by viewModels()
    lateinit var moviesAdapter: MoviesAdapter
    var pastVisibleItems = 0
    var visibleItemCount = 0
    lateinit var layoutManager: LinearLayoutManager
    var method = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.let {
            method = it.getInt(METHOD)
        }
        // Inflate the layout for this fragment
        observe(viewModel.movie, ::observeMovies)
        return inflater.inflate(R.layout.fragment_movies, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        askForMovies()
    }

    private fun askForMovies(){
        if (method == 0)
            viewModel.getMovies(requireActivity().isConnectionAvailable())
        else
            viewModel.getTopRated(requireActivity().isConnectionAvailable())
    }

    private fun setupView() {
        moviesAdapter = MoviesAdapter(requireActivity())
        layoutManager = LinearLayoutManager(requireActivity())
        movies_recycler.layoutManager = layoutManager

        moviesAdapter.setClickListener(
            object : GenericRecyclerViewAdapter.OnViewHolderClickListener<Movie> {
                override fun onClick(view: View, position: Int, item: Movie?) {
                    item?.let {
                        val intent = Intent(requireActivity(), MovieDetailActivity::class.java)
                        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                            requireActivity(),
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
            setDuration(1000)
            setInterpolator(OvershootInterpolator())
            setFirstOnly(false)
        }
        movies_recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    visibleItemCount = layoutManager.childCount
                    pastVisibleItems = layoutManager.findFirstVisibleItemPosition()
                    if ((visibleItemCount + pastVisibleItems) >= moviesAdapter.itemCount) {
                        askForMovies()
                    }
                }
            }
        })
    }

    private fun observeMovies(list: List<Movie>) {
        moviesAdapter.addAll(list)
        val internet = requireActivity().isConnectionAvailable()
        if (internet) {
            detailViewModel.saveMovieDetails(list, internet)
        }
    }

    companion object {

        const val METHOD = "METHOD"
        const val METHOD_POPULAR = 0
        const val METHOD_TOP_RATED = 1

        fun newInstance(method: Int = 0) =
            MoviesFragment().apply {
                arguments = Bundle().apply {
                    putInt(METHOD, method)
                }
            }
    }
}