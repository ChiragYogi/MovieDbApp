package com.example.moviedbapp.ui.tvshow


import androidx.lifecycle.*
import com.example.moviedbapp.modal.cast.CastResponse
import com.example.moviedbapp.modal.review.ReviewResponse
import com.example.moviedbapp.modal.tvshow.TvShowResponse
import com.example.moviedbapp.modal.tvshow.details.TvShowDetailResponse
import com.example.moviedbapp.repo.TmdbRepository
import com.example.moviedbapp.utiles.Resource
import kotlinx.coroutines.*


class TvShowDetailViewModel(private val repo: TmdbRepository) :
    ViewModel() {

    private val _tvDetail: MutableLiveData<Resource<TvShowDetailResponse>> = MutableLiveData()
    val tvDetail: LiveData<Resource<TvShowDetailResponse>> get() = _tvDetail

    private val _tvReview: MutableLiveData<Resource<ReviewResponse>> = MutableLiveData()
    val tvReview: LiveData<Resource<ReviewResponse>> get() = _tvReview

    private val _tvCast: MutableLiveData<Resource<CastResponse>> = MutableLiveData()
    val tvCast: LiveData<Resource<CastResponse>> get() = _tvCast

    fun getTvShowId(tvId: Int){
        getTvData(tvId)
    }


    private fun getTvData(tvId: Int) = viewModelScope.launch {

        supervisorScope {

            val tvDetailCall = async { repo.getTvDetails(tvId) }

            val tvReviewCall = async { repo.getTvReview(tvId) }

            val tvCastCall = async { repo.getTvCast(tvId) }


            _tvDetail.postValue(Resource.Loading())
            try {
                val response = tvDetailCall.await()
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    _tvDetail.postValue(Resource.Success(result))
                } else {
                    _tvDetail.postValue(Resource.Error(response.message()))
                }
            } catch (e: Exception) {
                _tvDetail.postValue(Resource.Error(e.message ?: "An error occurred"))
            }

            _tvReview.postValue(Resource.Loading())
            try {

                val response = tvReviewCall.await()
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    _tvReview.postValue(Resource.Success(result))
                } else {
                    _tvReview.postValue(Resource.Error(response.message()))
                }
            } catch (e: Exception) {
                _tvReview.postValue(Resource.Error(e.message ?: "An error occurred"))
            }

            _tvCast.postValue(Resource.Loading())
            try {
                val response = tvCastCall.await()
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    _tvCast.postValue(Resource.Success(result))
                } else {
                    _tvCast.postValue(Resource.Error(response.message()))
                }
            } catch (e: Exception) {
                _tvCast.postValue(Resource.Error(e.message ?: "An error occurred"))
            }




        }

    }
}


/*  private fun hasInternetConnection(): Boolean {
      val connectivityManager = getApplication<TMDBApplication>().getSystemService(
          Context.CONNECTIVITY_SERVICE
      ) as ConnectivityManager
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
          val activeNetwork = connectivityManager.activeNetwork ?: return false
          val capabilities =
              connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
          return when {
              capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
              capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
              capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
              else -> false
          }
      } else {
          connectivityManager.activeNetworkInfo?.run {
              return when (type) {
                  ConnectivityManager.TYPE_WIFI -> true
                  ConnectivityManager.TYPE_MOBILE -> true
                  ConnectivityManager.TYPE_ETHERNET -> true
                  else -> false
              }
          }
      }
      return false

  }
}

*/












