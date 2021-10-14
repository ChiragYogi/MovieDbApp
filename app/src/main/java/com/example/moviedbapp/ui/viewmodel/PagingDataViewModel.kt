package com.example.moviedbapp.ui.viewmodel


import android.util.Log
import androidx.lifecycle.*
import androidx.paging.*

import com.example.moviedbapp.modal.movie.MovieList
import com.example.moviedbapp.modal.tvshow.TvShowList
import com.example.moviedbapp.repo.PagingSourceForMovie
import com.example.moviedbapp.repo.PagingSourceForTvShow

import com.example.moviedbapp.repo.TmdbRepository
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


class PagingDataViewModel(repo: TmdbRepository) : ViewModel() {


    val movieList:  LiveData<PagingData<MovieList>> =
         repo.getPopularMovie().cachedIn(viewModelScope)


    val tvShowList: LiveData<PagingData<TvShowList>> =
        repo.getPopularTvShow().cachedIn(viewModelScope)




}