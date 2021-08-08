package mx.malain.movieapp.di

import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import mx.malain.movieapp.networking.MoviesService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideMoviesService(): MoviesService {
        return Retrofit.Builder()
            .baseUrl( "https://api.themoviedb.org/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))

            //.client(getHTTPClient())
            .build()
            .create(MoviesService::class.java)
    }
    val apiKey = "214edc3b36d08224b8548348b05f8656"
    val token = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIyMTRlZGMzYjM2ZDA4MjI0Yjg1NDgzNDhiMDVmODY1NiIsInN1YiI6IjYxMGM5NmU3NzcxOWQ3MDAyY2E2MWUxNiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.KISQ_Ds1qVWnAbGx-q3qktCsGAWXeNU1f6kIkKIXaHQ"

    private fun getHTTPClient(): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor { chain ->
            val originalRequest = chain.request()

            val request = originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .header("Content-Type", "application/json;charset=utf-8")
                .method(originalRequest.method(), originalRequest.body())
                .build()
            chain.proceed(request)
        }
        return httpClient.build()
    }
}