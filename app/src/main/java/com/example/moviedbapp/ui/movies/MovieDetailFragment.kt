package com.example.moviedbapp.ui.movies

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviedbapp.R
import com.example.moviedbapp.databinding.FragmentMovieDetailBinding
import com.example.moviedbapp.modal.movie.MovieResponseWithAllDetail
import com.example.moviedbapp.ui.adepter.CastAdepter
import com.example.moviedbapp.ui.adepter.CrewAdepter
import com.example.moviedbapp.ui.adepter.ReviewAdepter
import com.example.moviedbapp.utiles.Constance
import com.example.moviedbapp.utiles.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailFragment : Fragment(R.layout.fragment_movie_detail) {

    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!
    private var mProgressDialog: Dialog? = null

    private val mViewModel by viewModels<MovieDetailViewModel>()

    private val castAdepter = CastAdepter()
    private val crewAdepter = CrewAdepter()
    private val reviewAdepter = ReviewAdepter()

    private val args: MovieDetailFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeMovieDetail()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMovieDetailBinding.bind(view)

        val movieArgs = args.movieArgs
        (activity as AppCompatActivity).supportActionBar?.title = movieArgs?.title
        val movieId = movieArgs?.id

        if (movieId != null) {
            mViewModel.getMovieId(movieId)
        }



        setUpCastRecyclerView()
        setUpCrewRecyclerView()
        setUpReviewRecyclerView()

    }

    private fun observeMovieDetail() {
        mViewModel.movieDetails.observe(this) { response ->
            when (response) {
                is Resource.Error -> {
                    showErrorsAndHideView(response.message ?: getString(R.string.error_message))
                }
                is Resource.Loading -> {
                    showCustomDialog()
                }
                is Resource.Success -> {
                    setDataInUi(response.data)
                }
            }
        }

    }

    private fun setDataInUi(data: MovieResponseWithAllDetail?) {
        hideCustomDialog()
        data?.movieDetailResponse?.let { movieResponse ->
            binding.apply {
                Glide.with(this@MovieDetailFragment)
                    .load("${Constance.POSTER_IMAGE_PATH_PREFIX}${movieResponse.poster_path}")
                    .fitCenter().error(R.drawable.ic_movie).into(posterImageView)
                averageVoteTxt.text = movieResponse.vote_average.toInt().toString()
                tagLine.text = movieResponse.tagline
                spokenLanguages.text =
                    movieResponse.spoken_languages.joinToString { it.english_name }
                if (movieResponse.overview.isNotEmpty()) {
                    tagDescription.text = movieResponse.overview
                } else {
                    binding.tagDescriptionError.visibility = View.VISIBLE
                }

            }
        }

        data?.reviewResponse?.let { movieReview ->
            if (movieReview.results.isNotEmpty()) {
                reviewAdepter.swapData(movieReview.results)
            } else {
                binding.reviewRvView.visibility = View.GONE
                binding.noReviewErrorTxt.visibility = View.VISIBLE
            }
        }

        data?.castResponse?.let { castResponse ->

            if (castResponse.crew.isNotEmpty()) {
                crewAdepter.swapData(castResponse.crew)
            } else {
                binding.noCrewErrorTxt.visibility = View.VISIBLE
            }
            if (castResponse.cast.isNotEmpty()) {
                castAdepter.swapData(castResponse.cast)
            } else {
                binding.noCastErrorTxt.visibility = View.VISIBLE
            }

        }
    }

    private fun showErrorsAndHideView(message: String) {
        hideCustomDialog()
        binding.apply {
            noCrewErrorTxt.visibility = View.VISIBLE
            binding.noReviewErrorTxt.visibility = View.VISIBLE
            binding.tagDescriptionError.visibility = View.VISIBLE
            binding.tagDescriptionError.text = message
            binding.noReviewErrorTxt.text = message
            noCrewErrorTxt.text = message

        }

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