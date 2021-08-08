package mx.malain.movieapp.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter
import kotlinx.android.synthetic.main.fragment_search.*
import mx.malain.movieapp.R
import mx.malain.movieapp.extensions.isConnectionAvailable
import mx.malain.movieapp.extensions.observe
import mx.malain.movieapp.models.Movie
import mx.malain.movieapp.utils.FragmentAdapter
import mx.malain.movieapp.utils.GenericRecyclerViewAdapter
import mx.malain.movieapp.utils.MoviesAdapter
import mx.malain.movieapp.viewmodel.SearchViewModel

@AndroidEntryPoint
class SearchFragment : Fragment(), FragmentAdapter.SearchLister {

    private val viewModel: SearchViewModel by viewModels()
    lateinit var moviesAdapter: MoviesAdapter
    lateinit var layoutManager: LinearLayoutManager
    var pastVisibleItems = 0
    var visibleItemCount = 0
    var lastQuery = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observe(viewModel.movies, ::observeResults)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    override fun onQueryUpdated(query: String) {
        if (requireActivity().isConnectionAvailable()) {
            if (query.isEmpty()){
                ly_no_connection.visibility = View.GONE
                ly_no_results.visibility = View.GONE
                ly_no_query.visibility = View.VISIBLE
                search_results.visibility = View.GONE
            }
            viewModel.search(query)
            lastQuery = query
        } else {
            ly_no_connection.visibility = View.VISIBLE
            ly_no_query.visibility = View.GONE
            ly_no_results.visibility = View.GONE
            search_results.visibility = View.GONE
        }
    }

    private fun setupView() {
        moviesAdapter = MoviesAdapter(requireActivity())
        layoutManager = LinearLayoutManager(requireActivity())
        search_results.layoutManager = layoutManager

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
        search_results.adapter = SlideInBottomAnimationAdapter(moviesAdapter).apply {
            setDuration(1000)
            setInterpolator(OvershootInterpolator())
            setFirstOnly(false)
        }
        search_results.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    visibleItemCount = layoutManager.childCount
                    pastVisibleItems = layoutManager.findFirstVisibleItemPosition()
                    if ((visibleItemCount + pastVisibleItems) >= moviesAdapter.itemCount) {
                        if (requireActivity().isConnectionAvailable())
                            viewModel.search(lastQuery)
                    }
                }
            }
        })
    }

    private fun observeResults(list: Pair<List<Movie>, Boolean>) {
        if (list.first.isEmpty()){
            ly_no_connection.visibility = View.GONE
            ly_no_query.visibility = View.GONE
            ly_no_results.visibility = View.VISIBLE
            search_results.visibility = View.GONE
        } else {
            ly_no_connection.visibility = View.GONE
            ly_no_query.visibility = View.GONE
            ly_no_results.visibility = View.GONE
            search_results.visibility = View.VISIBLE
        }
        if (list.second)
            moviesAdapter.setNewContent(list.first)
        else
            moviesAdapter.addAll(list.first)
    }

    companion object {
        fun newInstance() =
            SearchFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}