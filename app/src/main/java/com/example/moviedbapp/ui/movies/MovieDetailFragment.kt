package com.example.moviedbapp.ui.movies

import android.app.Dialog
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviedbapp.R
import com.example.moviedbapp.TMDBApplication
import com.example.moviedbapp.databinding.FragmentMovieDetailBinding
import com.example.moviedbapp.ui.adepter.CastAdepter
import com.example.moviedbapp.ui.adepter.CrewAdepter
import com.example.moviedbapp.ui.adepter.ReviewAdepter
import com.example.moviedbapp.ui.viewmodel.TMDBViewModelProvider
import com.example.moviedbapp.utiles.Constance
import com.example.moviedbapp.utiles.Resource


class MovieDetailFragment : Fragment(R.layout.fragment_movie_detail) {

    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!
    private var mProgressDialog: Dialog? = null
    private val mViewModel: MovieDetailViewModel by viewModels {
        TMDBViewModelProvider((activity?.application as TMDBApplication).repo)
    }

    private val castAdepter = CastAdepter()
    private val crewAdepter = CrewAdepter()
    private val reviewAdepter = ReviewAdepter()

    private val args: MovieDetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMovieDetailBinding.bind(view)

        val movieArgs = args.movieArgs
        (activity as AppCompatActivity).supportActionBar?.title = movieArgs?.title
        val movieId = movieArgs?.id

        if (movieId != null) {
            mViewModel.getMovieData(movieId)
        }

        observerDetailData()
        observerReviewData()
        observerCastData()

        setUpCastRecyclerView()
        setUpCrewRecyclerView()
        setUpReviewRecyclerView()

    }

    private fun setUpReviewRecyclerView() {

        binding.apply {
            reviewRvView.layoutManager =
                LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            reviewRvView.adapter = reviewAdepter
            reviewRvView.setHasFixedSize(true)
            reviewRvView.isNestedScrollingEnabled = true
        }
    }

    private fun setUpCrewRecyclerView() {

        binding.apply {

            crewRvView.layoutManager =
                LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            crewRvView.adapter = crewAdepter
            crewRvView.setHasFixedSize(true)
            crewRvView.isNestedScrollingEnabled = true
        }
    }

    private fun setUpCastRecyclerView() {

        binding.apply {


            castRvView.layoutManager =
                LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            castRvView.adapter = castAdepter
            castRvView.setHasFixedSize(true)
            castRvView.isNestedScrollingEnabled = false
        }
    }


    private fun observerCastData() {
        mViewModel.movieCast.observe(viewLifecycleOwner, { castResponse ->
            when (castResponse) {
                is Resource.Error -> {
                    hideCustomDialog()
                    castResponse.message?.let {
                        binding.noCrewErrorTxt.visibility = View.VISIBLE
                        binding.noCrewErrorTxt.text = it
                    }
                }

                is Resource.Loading -> {
                    showCustomDialog()
                }

                is Resource.Success -> {
                    hideCustomDialog()
                    castResponse.data?.let { castResponseData ->

                        if (castResponseData.crew.isNotEmpty()) {
                            crewAdepter.swapData(castResponseData.crew)
                        } else {
                            binding.noCrewErrorTxt.visibility = View.VISIBLE
                        }
                        if (castResponseData.cast.isNotEmpty()) {
                            castAdepter.swapData(castResponseData.cast)
                        } else {

                            binding.noCastErrorTxt.visibility = View.VISIBLE
                        }
                    }

                }
            }

        })

    }


    private fun observerReviewData() {
        mViewModel.movieReview.observe(viewLifecycleOwner, { reviewResponse ->
            when (reviewResponse) {
                is Resource.Error -> {
                    hideCustomDialog()
                    reviewResponse.message?.let {
                        binding.noReviewErrorTxt.visibility = View.VISIBLE
                        binding.noReviewErrorTxt.text = it
                    }
                }

                is Resource.Loading -> {
                    showCustomDialog()
                }

                is Resource.Success -> {
                    hideCustomDialog()
                    reviewResponse.data?.let { reviewData ->
                        if (reviewData.total_pages > 0) {
                            reviewAdepter.swapData(reviewData.results)
                        } else {
                            binding.reviewRvView.visibility = View.GONE
                            binding.noReviewErrorTxt.visibility = View.VISIBLE
                        }
                    }
                }

            }

        })
    }

    private fun observerDetailData() {
        mViewModel.movieDetail.observe(viewLifecycleOwner, { detailResponse ->

            when (detailResponse) {
                is Resource.Error -> {
                    hideCustomDialog()
                    detailResponse.message?.let {
                        binding.tagDescriptionError.visibility = View.VISIBLE
                        binding.tagDescriptionError.text = it
                    }
                }

                is Resource.Loading -> {
                    showCustomDialog()
                }

                is Resource.Success -> {
                    hideCustomDialog()
                    detailResponse.data?.let { movieDetail ->
                        binding.apply {
                            Glide.with(this@MovieDetailFragment)
                                .load("${Constance.POSTER_IMAGE_PATH_PREFIX}${movieDetail.poster_path}")
                                .fitCenter().error(R.drawable.ic_movie).into(posterImageView)
                            averageVoteTxt.text = movieDetail.vote_average.toInt().toString()
                            tagLine.text = movieDetail.tagline
                            spokenLanguages.text =
                                movieDetail.spoken_languages.joinToString { it.english_name }
                            if (movieDetail.overview.isNotEmpty()) {
                                tagDescription.text = movieDetail.overview
                            } else {
                                binding.tagDescriptionError.visibility = View.VISIBLE
                            }

                        }
                    }
                }

            }

        })
    }

    private fun hideCustomDialog() {
        mProgressDialog?.dismiss()
    }

    private fun showCustomDialog() {
        mProgressDialog = Dialog(requireContext())
        mProgressDialog?.let {
            it.setContentView(R.layout.custome_progress_bar)
            it.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}