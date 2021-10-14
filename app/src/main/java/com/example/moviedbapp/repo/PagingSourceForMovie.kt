package com.example.moviedbapp.repo

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.moviedbapp.modal.movie.MovieList
import com.example.moviedbapp.network.TmdbNetworkClient.apiService
import com.example.moviedbapp.utiles.Constance
import com.example.moviedbapp.utiles.Constance.API_KEY
import retrofit2.HttpException
import java.io.IOException


class PagingSourceForMovie():
    PagingSource<Int,MovieList>() {


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieList> {

        val position = params.key ?: Constance.TMDB_PAGE_NUMBER_START
        return try {
            val responce = apiService.getPopularMovie(API_KEY,position)
            val result = responce.results
            Log.d("responce and result","$result")
            Log.d("responce and result", Thread.currentThread().name)

            LoadResult.Page(
                data = result,
                prevKey = if (position == Constance.TMDB_PAGE_NUMBER_START) null else position - 1,
                nextKey = if(position < responce.total_pages) responce.page.plus(1) else null,
            )
        }catch (exception: IOException){
            return LoadResult.Error(exception)
        }catch (exception: HttpException){
            return LoadResult.Error(exception)
        }catch (e: Exception){
            return LoadResult.Error(e)
        }

    }

    override fun getRefreshKey(state: PagingState<Int, MovieList>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)

        }
    }


}