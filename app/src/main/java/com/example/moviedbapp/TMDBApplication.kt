package com.example.moviedbapp

import android.app.Application
import com.example.moviedbapp.network.TmdbNetworkClient
import com.example.moviedbapp.repo.TmdbRepository

class TMDBApplication: Application() {

   val repo by lazy { TmdbRepository() }
}