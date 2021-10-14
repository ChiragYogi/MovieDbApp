package com.example.moviedbapp.network

import com.example.moviedbapp.modal.cast.CastResponse
import com.example.moviedbapp.modal.movie.MovieResponse
import com.example.moviedbapp.modal.movie.details.MovieDetailResponse
import com.example.moviedbapp.modal.review.ReviewResponse
import com.example.moviedbapp.modal.tvshow.TvShowResponse
import com.example.moviedbapp.modal.tvshow.details.TvShowDetailResponse
import retrofit2.Response


import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbNetworkCall {

    @GET("movie/popular")
    suspend fun getPopularMovie(
        @Query("api_key")
        apiKey: String,
        @Query("page")
        page: Int
    ): MovieResponse

    @GET("tv/popular")
    suspend fun getPopularTvShow(
        @Query("api_key")
        apiKey: String,
        @Query("page")
        page: Int,
    ): TvShowResponse

    @GET("tv/{tv_id}")
    suspend fun getTvShowDetails(
        @Path("tv_id")
        tvId: Int,
        @Query("api_key")
        apiKey: String
    ): Response<TvShowDetailResponse>

    @GET("tv/{tv_id}/reviews")
    suspend fun getTvReview(
        @Path("tv_id")
        tvId: Int,
       @Query("api_key")
        apiKey: String
    ): Response<ReviewResponse>

    @GET("tv/{tv_id}/credits")
    suspend fun getCastAndCrewForTvSHow(
        @Path("tv_id")
        tvId: Int,
        @Query("api_key")
        apiKey: String
    ): Response<CastResponse>




    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id")
        movieId: Int,
        @Query("api_key")
        apiKey: String
    ): Response<MovieDetailResponse>

    @GET("movie/{movie_id}/reviews")
    suspend fun getMovieReview(
        @Path("movie_id")
        movieId: Int,
        @Query("api_key")
        apiKey: String
    ): Response<ReviewResponse>

    @GET("movie/{movie_id}/credits")
    suspend fun getCastAndCrewForMovie(
        @Path("movie_id")
        movieId: Int,
        @Query("api_key")
        apiKey: String
    ): Response<CastResponse>




}