package com.example.moviedbapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.moviedbapp.repo.TmdbRepository
import com.example.moviedbapp.ui.movies.MovieDetailViewModel
import com.example.moviedbapp.ui.tvshow.TvShowDetailViewModel

class TMDBViewModelProvider(private val repo: TmdbRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PagingDataViewModel::class.java)) {
            return PagingDataViewModel(repo) as T
        }
        if (modelClass.isAssignableFrom(TvShowDetailViewModel::class.java)) {
            return TvShowDetailViewModel(repo) as T
        }
        if (modelClass.isAssignableFrom(MovieDetailViewModel::class.java)){
            return MovieDetailViewModel(repo) as T
        }
        throw IllegalArgumentException(String.format("No ViewModelClass Found"))

    }
}
