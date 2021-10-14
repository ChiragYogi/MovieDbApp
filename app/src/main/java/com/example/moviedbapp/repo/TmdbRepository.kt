package com.example.moviedbapp.repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.moviedbapp.modal.cast.CastResponse
import com.example.moviedbapp.modal.movie.MovieList
import com.example.moviedbapp.modal.movie.details.MovieDetailResponse
import com.example.moviedbapp.modal.review.ReviewResponse
import com.example.moviedbapp.modal.tvshow.TvShowResponse
import com.example.moviedbapp.modal.tvshow.details.TvShowDetailResponse
import com.example.moviedbapp.network.TmdbNetworkClient.apiService
import com.example.moviedbapp.utiles.Constance.API_KEY
import com.example.moviedbapp.utiles.Resource
import kotlinx.coroutines.*


class TmdbRepository {


     fun getPopularTvShow() =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PagingSourceForTvShow() }
        ).liveData


     fun getPopularMovie(): LiveData<PagingData<MovieList>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PagingSourceForMovie() }
        ).liveData
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

