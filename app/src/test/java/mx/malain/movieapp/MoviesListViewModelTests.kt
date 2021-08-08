package mx.malain.movieapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import mx.malain.movieapp.datasource.MoviesLocalDataSource
import mx.malain.movieapp.datasource.MoviesRemoteDataSource
import mx.malain.movieapp.models.Movie
import mx.malain.movieapp.repositories.MoviesRepo
import mx.malain.movieapp.viewmodel.MoviesListViewModel
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MoviesListViewModelTests {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: MoviesListViewModel

    private val moviesRemoteDataSource: MoviesRemoteDataSource = mock()
    private val moviesLocalDataSource: MoviesLocalDataSource = mock()
    private val movieListRepo: MoviesRepo =
        MoviesRepo(moviesRemoteDataSource, moviesLocalDataSource)

    @Before
    fun before() {
        viewModel = MoviesListViewModel(movieListRepo)
    }

    @Test
    fun itShouldReturnSomeMovies() = runBlocking {
        whenever(moviesRemoteDataSource.getMovies(1)).thenReturn(Seeds.movies)
        viewModel.getMovies(true)
        val expectedResult = Seeds.movies
        val result = LiveDataTestUtil.getValue(viewModel.movie)
        Assert.assertEquals(expectedResult, result)
    }

    @Test
    fun itShouldReturnEmptyListOnPageTwo() = runBlocking {
        whenever(moviesRemoteDataSource.getMovies(2)).thenReturn(arrayListOf())
        viewModel.currentPage = 2
        viewModel.getMovies(true)
        val expectedResult = arrayListOf<Movie>()
        val result = LiveDataTestUtil.getValue(viewModel.movie)
        Assert.assertEquals(expectedResult, result)
    }

    @Test
    fun itShouldGetDataFromDisk() = runBlocking {
        whenever(moviesLocalDataSource.getMovies(1)).thenReturn(Seeds.moviesOffline)
        viewModel.getMovies(false)
        val expectedData = Seeds.moviesOffline
        val result = LiveDataTestUtil.getValue(viewModel.movie)
        Assert.assertEquals(expectedData, result)
    }

    @Test
    fun itShouldPrefferFetchDataFromNetwork() = runBlocking {
        whenever(moviesLocalDataSource.getMovies(1)).thenReturn(Seeds.moviesOffline)
        whenever(moviesRemoteDataSource.getMovies(1)).thenReturn(Seeds.movies)
        viewModel.getMovies(true)
        val expectedResult = Seeds.moviesOffline
        val result = LiveDataTestUtil.getValue(viewModel.movie)
        Assert.assertNotEquals(expectedResult, result)
    }
}