package com.example.moviedbapp.network

import com.example.moviedbapp.utiles.Constance.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object TmdbNetworkClient {




    private val retrofit by lazy {

            val logging = HttpLoggingInterceptor()
                logging.setLevel(HttpLoggingInterceptor.Level.BODY)

            val loggingClient = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()

           Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(loggingClient)
            .build()
    }

    val apiService: TmdbNetworkService by lazy {
        retrofit.create(TmdbNetworkService::class.java)

    }


}