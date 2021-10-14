package com.example.moviedbapp.modal.tvshow.details

data class TvShowDetailResponse(
    val backdrop_path: String,
    val created_by: List<CreatedBy>,
    val genres: List<Genre>,
    val homepage: String,
    val id: Int,
    val languages: List<String>,
    val name: String,
    val networks: List<Network>,
    val number_of_seasons: Int,
    val original_name: String,
    val original_language: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val seasons: List<Season>,
    val spoken_languages: List<SpokenLanguage>,
    val tagline: String,
    val vote_average: Double,
    val vote_count: Int
)