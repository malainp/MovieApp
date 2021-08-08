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
import mx.malain.movieapp.repositories.MoviesRepo
import javax.inject.Inject

@HiltViewModel
class MoviesListViewModel @Inject constructor(
    private val repository: MoviesRepo
) : ViewModel() {
    private val mMoviesList: MutableLiveData<List<Movie>> = MutableLiveData()

    val movie: LiveData<List<Movie>> get() = mMoviesList

    var currentPage = 1

    private var job: Job? = null
    private var jobTopRated: Job? = null

    fun getMovies(isConnected: Boolean) {
        if (job?.isActive == true)
            return
        job = getMoviesExec(currentPage++, isConnected)

    }

    fun getTopRated(isConnected: Boolean) {
        if (jobTopRated?.isActive == true)
            return
        jobTopRated = getTopRatedExec(currentPage++, isConnected)
    }

    private fun getMoviesExec(page: Int = 1, isConnected: Boolean) =
        GlobalScope.launch(Dispatchers.IO) {
            val movies = repository.getMovies(page, isConnected)
            mMoviesList.postValue(movies)
        }

    private fun getTopRatedExec(page: Int = 1, isConnected: Boolean) =
        GlobalScope.launch(Dispatchers.IO) {
            val movies = repository.getTopRatedMovies(page, isConnected)
            mMoviesList.postValue(movies)
        }
}