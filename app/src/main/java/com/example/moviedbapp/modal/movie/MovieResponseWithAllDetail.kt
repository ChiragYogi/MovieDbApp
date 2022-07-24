package com.example.moviedbapp.modal.movie

import com.example.moviedbapp.modal.cast.CastResponse
import com.example.moviedbapp.modal.movie.details.MovieDetailResponse
import com.example.moviedbapp.modal.review.ReviewResponse

data class MovieResponseWithAllDetail(
    val movieDetailResponse: MovieDetailResponse,
    val reviewResponse: ReviewResponse,
    val castResponse: CastResponse
)