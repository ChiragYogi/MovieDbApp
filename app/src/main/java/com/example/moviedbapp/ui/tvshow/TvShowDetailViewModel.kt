package com.example.moviedbapp.ui.tvshow


import androidx.lifecycle.*
import com.example.moviedbapp.modal.cast.CastResponse
import com.example.moviedbapp.modal.review.ReviewResponse
import com.example.moviedbapp.modal.tvshow.TvShowResponseWithAllDetail
import com.example.moviedbapp.modal.tvshow.details.TvShowDetailResponse
import com.example.moviedbapp.repo.MainTmdbRepository
import com.example.moviedbapp.repo.TmdbRepository
import com.example.moviedbapp.utiles.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject


@HiltViewModel
class TvShowDetailViewModel @Inject constructor( private val tmdbRepository: TmdbRepository) :
    ViewModel() {


    private val _tvDetails: MutableLiveData<Resource<TvShowResponseWithAllDetail>> = MutableLiveData()
    val tvDetails: LiveData<Resource<TvShowResponseWithAllDetail>> get() = _tvDetails

    fun getTvShowId(tvId: Int){
         getTvData(tvId)
    }




    private fun getTvData(tvId: Int) = viewModelScope.launch {
        _tvDetails.value = Resource.Loading()
        val response = tmdbRepository.getTvShowDetailById(tvId)
        _tvDetails.postValue(response)
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












