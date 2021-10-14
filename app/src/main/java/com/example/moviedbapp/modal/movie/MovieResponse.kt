package com.example.moviedbapp.modal.movie

data class MovieResponse(
    val page: Int,
    val results: List<MovieList>,
    val total_pages: Int,
    val total_results: Int
)