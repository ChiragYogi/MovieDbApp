package com.example.moviedbapp.modal.tvshow

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class TvShowList(
    val id: Int,
    val name: String,
    val original_name: String,
    val poster_path: String
): Parcelable