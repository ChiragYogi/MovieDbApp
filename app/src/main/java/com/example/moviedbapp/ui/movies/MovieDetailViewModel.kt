package com.example.moviedbapp.ui.movies

import androidx.lifecycle.*
import com.example.moviedbapp.modal.cast.CastResponse
import com.example.moviedbapp.modal.movie.MovieResponseWithAllDetail
import com.example.moviedbapp.modal.movie.details.MovieDetailResponse
import com.example.moviedbapp.modal.review.ReviewResponse
import com.example.moviedbapp.modal.tvshow.TvShowResponseWithAllDetail
import com.example.moviedbapp.repo.MainTmdbRepository
import com.example.moviedbapp.repo.TmdbRepository
import com.example.moviedbapp.utiles.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(private val repo: TmdbRepository) : ViewModel() {


    private val _movieDetails: MutableLiveData<Resource<MovieResponseWithAllDetail>> =
        MutableLiveData()
    val movieDetails: LiveData<Resource<MovieResponseWithAllDetail>> get() = _movieDetails


    fun getMovieId(movieId: Int) {
        getMovieData(movieId)
    }


    fun getMovieData(movieId: Int) = viewModelScope.launch {
        _movieDetails.value = Resource.Loading()
        val response = repo.getMovieDetailById(movieId)
        _movieDetails.postValue(response)
    }
}




