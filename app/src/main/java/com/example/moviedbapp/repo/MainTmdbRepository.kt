package com.example.moviedbapp.repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.moviedbapp.BuildConfig
import com.example.moviedbapp.di.IoDispatcher
import com.example.moviedbapp.modal.cast.CastResponse
import com.example.moviedbapp.modal.movie.MovieList
import com.example.moviedbapp.modal.movie.MovieResponseWithAllDetail
import com.example.moviedbapp.modal.movie.details.MovieDetailResponse
import com.example.moviedbapp.modal.review.ReviewResponse
import com.example.moviedbapp.modal.tvshow.TvShowList
import com.example.moviedbapp.modal.tvshow.TvShowResponse
import com.example.moviedbapp.modal.tvshow.TvShowResponseWithAllDetail
import com.example.moviedbapp.modal.tvshow.details.TvShowDetailResponse
import com.example.moviedbapp.network.TmdbNetworkClient.apiService
import com.example.moviedbapp.network.TmdbNetworkService
import com.example.moviedbapp.utiles.Constance.API_KEY
import com.example.moviedbapp.utiles.Resource
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import kotlin.coroutines.coroutineContext


class MainTmdbRepository @Inject constructor(
    private val tmdbNetworkService: TmdbNetworkService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) :TmdbRepository {


    override fun getPopularTvShow(): Pager<Int, TvShowList> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory =
            { PagingSourceForTvShow() }
        )
    }

    override fun getPopularMovie(): Pager<Int, MovieList> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PagingSourceForMovie() }
        )
    }


    override suspend fun getTvShowDetailById(tvId: Int): Resource<TvShowResponseWithAllDetail> {
        return try {
            withContext(ioDispatcher + SupervisorJob()) {

                val tvDetailCall = async { tmdbNetworkService.getTvShowDetails(tvId,BuildConfig.API_KEY) }

                val tvReviewCall = async { tmdbNetworkService.getTvReview(tvId,BuildConfig.API_KEY) }

                val tvCastCall = async { tmdbNetworkService.getCastAndCrewForTvSHow(tvId,BuildConfig.API_KEY) }


                val detailResponse = tvDetailCall.await()
                val reviewResponse = tvReviewCall.await()
                val castResponse = tvCastCall.await()

                if (detailResponse.isSuccessful && detailResponse.body() != null
                    && reviewResponse.isSuccessful && reviewResponse.body() != null
                    && castResponse.isSuccessful && castResponse.body() != null
                ) {

                    Resource.Success(
                        TvShowResponseWithAllDetail(
                            detailResponse.body()!!,
                            reviewResponse.body()!!,
                            castResponse.body()!!
                        )
                    )
                } else {
                    val error =
                        "${detailResponse.errorBody()} ${reviewResponse.errorBody()} ${castResponse.errorBody()}"
                    Resource.Error(error)
                }

            }

        } catch (io: IOException) {
            Resource.Error(io.message.toString())
        } catch (http: HttpException) {
            Resource.Error(http.message.toString())
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }


    }

    override suspend fun getMovieDetailById(movieId: Int): Resource<MovieResponseWithAllDetail> {
        return try {
            withContext(ioDispatcher + SupervisorJob()){

                val movieDetailResponse = async { tmdbNetworkService.getMovieDetails(movieId,BuildConfig.API_KEY) }

                val movieReviewCall = async { tmdbNetworkService.getMovieReview(movieId,BuildConfig.API_KEY) }

                val movieCastCall = async { tmdbNetworkService.getCastAndCrewForMovie(movieId,BuildConfig.API_KEY) }


                val detailResponse = movieDetailResponse.await()
                val reviewResponse = movieReviewCall.await()
                val castResponse = movieCastCall.await()

                if (detailResponse.isSuccessful && detailResponse.body() != null
                    && reviewResponse.isSuccessful && reviewResponse.body() != null
                    && castResponse.isSuccessful && castResponse.body() != null
                ) {

                    Resource.Success(
                        MovieResponseWithAllDetail(
                            detailResponse.body()!!,
                            reviewResponse.body()!!,
                            castResponse.body()!!
                        )
                    )
                } else {
                    val error =
                        "${detailResponse.errorBody()} ${reviewResponse.errorBody()} ${castResponse.errorBody()}"
                    Resource.Error(error)
                }

            }

        } catch (io: IOException) {
            Resource.Error(io.message.toString())
        } catch (http: HttpException) {
            Resource.Error(http.message.toString())
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }


    suspend fun getTvDetails(tvId: Int) =
        withContext(Dispatchers.IO) { apiService.getTvShowDetails(tvId, API_KEY) }

    suspend fun getTvReview(tvId: Int) =
        withContext(Dispatchers.IO) { apiService.getTvReview(tvId, API_KEY) }

    suspend fun getTvCast(tvId: Int) =
        withContext(Dispatchers.IO) { apiService.getCastAndCrewForTvSHow(tvId, API_KEY) }

    suspend fun getMovieDetails(movieId: Int) =
        withContext(Dispatchers.IO) { apiService.getMovieDetails(movieId, API_KEY) }

    suspend fun getMovieReview(movieId: Int) =
        withContext(Dispatchers.IO) { apiService.getMovieReview(movieId, API_KEY) }

    suspend fun getMovieCast(movieId: Int) =
        withContext(Dispatchers.IO) { apiService.getCastAndCrewForMovie(movieId, API_KEY) }


}

