package mx.malain.movieapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import mx.malain.movieapp.models.Movie
import mx.malain.movieapp.models.movie_detail.MovieDetail
import mx.malain.movieapp.models.videos.Video
import mx.malain.movieapp.repositories.MovieDetailRepo
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val repository: MovieDetailRepo
) : ViewModel() {
    private val mMovieDetail: MutableLiveData<MovieDetail> = MutableLiveData()
    private val mMovieVideos: MutableLiveData<List<Video>> = MutableLiveData()

    val movieDetail: LiveData<MovieDetail> get() = mMovieDetail
    val videos: LiveData<List<Video>> get() = mMovieVideos

    private var job: Job? = null
    private var saveJobn: Job? = null
    private var jobVideos: Job? = null

    fun getMovieDetail(id: Long, isConnectionAvailable: Boolean) {
        if (job?.isActive == true)
            return
        job = getMovieDetailExec(id, isConnectionAvailable)
    }

    fun saveMovieDetails(details: List<Movie>, isConnectionAvailable: Boolean) {
        if (saveJobn?.isActive == true)
            return
        saveJobn = saveMovieDetailsExec(details, isConnectionAvailable)
    }


    fun getMovieVideos(id: Long, isConnectionAvailable: Boolean) {
        if (jobVideos?.isActive == true)
            return
        jobVideos = getMovieVideosExec(id, isConnectionAvailable)
    }

    private fun getMovieDetailExec(id: Long, isConnectionAvailable: Boolean) =
        GlobalScope.launch(Dispatchers.IO) {
            val movieDetail = repository.getMovieDetail(id, isConnectionAvailable)
            movieDetail?.let {
                mMovieDetail.postValue(it)
            }
        }

    private fun getMovieVideosExec(id: Long, isConnectionAvailable: Boolean) =
        GlobalScope.launch(Dispatchers.IO) {
            val videos = repository.getMovieVideos(id, isConnectionAvailable)
            videos?.let {
                mMovieVideos.postValue(it)
            }
        }

    private fun saveMovieDetailsExec(details: List<Movie>, isConnectionAvailable: Boolean) =
        GlobalScope.launch(Dispatchers.IO) {
            details.forEach {
                repository.getMovieDetail(it.id, isConnectionAvailable)
                repository.getMovieVideos(it.id, isConnectionAvailable)
            }
        }
}