package mx.malain.movieapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import mx.malain.movieapp.models.Movie
import mx.malain.movieapp.repositories.SearchRepo
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: SearchRepo
) : ViewModel() {
    private val mMovieList: MutableLiveData<Pair<List<Movie>, Boolean>> = MutableLiveData()

    val movies: LiveData<Pair<List<Movie>, Boolean>> get() = mMovieList

    var currentPage = 1

    private var lastQuery = ""

    private var job: Job? = null

    fun search(query: String) {
        if (job?.isActive == true)
            return
        if (lastQuery == query)
            currentPage++
        else {
            currentPage = 1
        }
        lastQuery = query
        job = searchMoviesExec(currentPage, query)
    }

    private fun searchMoviesExec(page: Int = 1, query: String) =
        GlobalScope.launch(Dispatchers.IO) {
            val movies = repository.search(query, page)
            mMovieList.postValue(Pair(movies, page == 1))
        }
}