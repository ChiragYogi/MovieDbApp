package com.example.moviedbapp

import android.app.Application
import com.example.moviedbapp.repo.MainTmdbRepository
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class TMDBApplication: Application()