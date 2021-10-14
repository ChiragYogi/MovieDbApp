package com.example.moviedbapp.modal.movie

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class MovieList(
    val id: Int,
    val original_title: String,
    val poster_path: String,
    val title: String
): Parcelable