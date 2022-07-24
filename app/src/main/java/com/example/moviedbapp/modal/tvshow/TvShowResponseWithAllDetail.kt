package com.example.moviedbapp.modal.tvshow

import com.example.moviedbapp.modal.cast.CastResponse
import com.example.moviedbapp.modal.review.ReviewResponse
import com.example.moviedbapp.modal.tvshow.details.TvShowDetailResponse

data class TvShowResponseWithAllDetail(
    val tvShowDetailResponse: TvShowDetailResponse,
    val reviewResponse: ReviewResponse,
    val castResponse: CastResponse
)
