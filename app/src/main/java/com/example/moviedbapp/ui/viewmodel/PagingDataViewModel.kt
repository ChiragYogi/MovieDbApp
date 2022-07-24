package com.example.moviedbapp.ui.viewmodel


import androidx.lifecycle.*
import androidx.paging.*

import com.example.moviedbapp.modal.movie.MovieList
import com.example.moviedbapp.modal.tvshow.TvShowList

import com.example.moviedbapp.repo.MainTmdbRepository
import com.example.moviedbapp.repo.TmdbRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class PagingDataViewModel @Inject constructor(tmdbRepository: TmdbRepository) : ViewModel() {


    val movieList:  LiveData<PagingData<MovieList>> =
        tmdbRepository.getPopularMovie().liveData.cachedIn(viewModelScope)


    val tvShowList: LiveData<PagingData<TvShowList>> =
        tmdbRepository.getPopularTvShow().liveData.cachedIn(viewModelScope)




}