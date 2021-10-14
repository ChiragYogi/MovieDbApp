package com.example.moviedbapp.modal.tvshow

data class TvShowResponse(
    val page: Int,
    val results: List<TvShowList>,
    val total_pages: Int,
    val total_results: Int
)