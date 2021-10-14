package com.example.moviedbapp.ui.movies

import androidx.lifecycle.*
import com.example.moviedbapp.modal.cast.CastResponse
import com.example.moviedbapp.modal.movie.MovieResponse
import com.example.moviedbapp.modal.movie.details.MovieDetailResponse
import com.example.moviedbapp.modal.review.ReviewResponse
import com.example.moviedbapp.repo.TmdbRepository
import com.example.moviedbapp.utiles.Resource
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

class MovieDetailViewModel(private val repo: TmdbRepository) : ViewModel() {

    private val _movieDetail: MutableLiveData<Resource<MovieDetailResponse>> = MutableLiveData()
    val movieDetail: LiveData<Resource<MovieDetailResponse>> get() = _movieDetail


    private val _movieReview: MutableLiveData<Resource<ReviewResponse>> = MutableLiveData()
    val movieReview: LiveData<Resource<ReviewResponse>> get() = _movieReview

    private val _movieCast: MutableLiveData<Resource<CastResponse>> = MutableLiveData()
    val movieCast: LiveData<Resource<CastResponse>> get() = _movieCast






    fun getMovieData(movieId: Int) = viewModelScope.launch {
            supervisorScope {


                val movieDetailCall = async { repo.getMovieDetails(movieId) }

                val movieReviewCall = async { repo.getMovieReview(movieId) }

                val movieCastCall = async { repo.getMovieCast(movieId) }




                _movieDetail.postValue(Resource.Loading())
                try {
                    val response = movieDetailCall.await()
                    val result = response.body()
                    if (response.isSuccessful && result != null) {
                        _movieDetail.postValue(Resource.Success(result))
                    } else {
                        _movieDetail.postValue(Resource.Error(response.message()))
                    }
                } catch (e: Exception) {
                    _movieDetail.postValue(Resource.Error(e.message ?: "An error occurred"))
                }

                _movieReview.postValue(Resource.Loading())
                try {

                    val response = movieReviewCall.await()
                    val result = response.body()
                    if (response.isSuccessful && result != null) {
                        _movieReview.postValue(Resource.Success(result))
                    } else {
                        _movieReview.postValue(Resource.Error(response.message()))
                    }
                } catch (e: Exception) {
                    _movieReview.postValue(Resource.Error(e.message ?: "An error occurred"))
                }

                _movieCast.postValue(Resource.Loading())
                try {
                    val response = movieCastCall.await()
                    val result = response.body()
                    if (response.isSuccessful && result != null) {
                        _movieCast.postValue(Resource.Success(result))
                    } else {
                        _movieCast.postValue(Resource.Error(response.message()))
                    }
                } catch (e: Exception) {
                    _movieCast.postValue(Resource.Error(e.message ?: "An error occurred"))
                }

            }
        }

    }




