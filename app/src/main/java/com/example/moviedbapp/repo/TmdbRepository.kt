package com.example.moviedbapp.repo

import androidx.paging.Pager
import androidx.paging.PagingData
import com.example.moviedbapp.modal.movie.MovieList
import com.example.moviedbapp.modal.movie.MovieResponseWithAllDetail
import com.example.moviedbapp.modal.tvshow.TvShowList
import com.example.moviedbapp.modal.tvshow.TvShowResponseWithAllDetail
import com.example.moviedbapp.modal.tvshow.details.TvShowDetailResponse
import com.example.moviedbapp.utiles.Resource
import retrofit2.Response

interface TmdbRepository {

    fun getPopularMovie(): Pager<Int,MovieList>

    fun getPopularTvShow(): Pager<Int,TvShowList>

    suspend fun getTvShowDetailById(tvId: Int): Resource<TvShowResponseWithAllDetail>

    suspend fun getMovieDetailById(movieId: Int): Resource<MovieResponseWithAllDetail>
}