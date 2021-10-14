package com.example.moviedbapp.ui.tvshow

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviedbapp.R
import com.example.moviedbapp.TMDBApplication
import com.example.moviedbapp.databinding.FragmentTvshowDetailesBinding

import com.example.moviedbapp.ui.adepter.CastAdepter
import com.example.moviedbapp.ui.adepter.CrewAdepter
import com.example.moviedbapp.ui.adepter.ReviewAdepter
import com.example.moviedbapp.ui.viewmodel.TMDBViewModelProvider
import com.example.moviedbapp.utiles.Constance
import com.example.moviedbapp.utiles.Resource

class TvShowDetailsFragment : Fragment(R.layout.fragment_tvshow_detailes) {

    private var _binding: FragmentTvshowDetailesBinding? = null
    private val binding get() = _binding!!
    private var mProgressDialog: Dialog? = null
    private val mViewModel: TvShowDetailViewModel by viewModels {
        TMDBViewModelProvider((activity?.application as TMDBApplication).repo)
    }
    private val args: TvShowDetailsFragmentArgs by navArgs()
    private val castAdepter = CastAdepter()
    private val crewAdepter = CrewAdepter()
    private val reviewAdepter = ReviewAdepter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTvshowDetailesBinding.bind(view)


        val tvArgs = args.tvShowArgs

        (activity as AppCompatActivity).supportActionBar?.title = tvArgs?.name


        val tvId = tvArgs?.id
        if (tvId != null) {
            mViewModel.getTvShowId(tvId)
            Log.d("tvId", "$tvId")
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
        mViewModel.tvCast.observe(viewLifecycleOwner, { castResponse ->
            when (castResponse) {
                is Resource.Error -> {
                    hideCustomDialog()
                    castResponse.message?.let {
                        binding.apply {
                            noCastErrorTxt.visibility = View.VISIBLE
                            noCrewErrorTxt.visibility = View.VISIBLE
                            noCastErrorTxt.text = it
                            noCrewErrorTxt.text = it
                        }

                    }
                }

                is Resource.Loading -> {
                    showCustomDialog()

                }

                is Resource.Success -> {
                    hideCustomDialog()
                    castResponse.data?.let { castAndCrewData ->

                        if (castAndCrewData.cast.isNotEmpty()) {
                            castAdepter.swapData(castAndCrewData.cast)
                        }else{
                            binding.noCastErrorTxt.visibility = View.VISIBLE
                        }
                        if (castAndCrewData.crew.isNotEmpty()) {
                            crewAdepter.swapData(castAndCrewData.crew)
                        } else {
                            binding.noCrewErrorTxt.visibility = View.VISIBLE
                        }


                    }
                }

            }
        })
    }


    private fun observerReviewData() {

        mViewModel.tvReview.observe(viewLifecycleOwner, { reviewResponse ->
            when (reviewResponse) {

                is Resource.Success -> {
                    hideCustomDialog()
                    reviewResponse.data?.let { reviewData ->
                        if (reviewData.total_results > 0) {
                            reviewAdepter.swapData(reviewData.results)
                        } else {
                            binding.reviewRvView.visibility = View.GONE
                            binding.noReviewErrorTxt.visibility = View.VISIBLE
                        }

                    }
                }
                is Resource.Error -> {
                    hideCustomDialog()
                    reviewResponse.message?.let {
                        binding.apply {
                            noReviewErrorTxt.visibility = View.VISIBLE
                            noReviewErrorTxt.text = it
                        }

                    }
                }

                is Resource.Loading -> {
                    showCustomDialog()
                }
            }
        })
    }

    private fun observerDetailData() {

        mViewModel.tvDetail.observe(viewLifecycleOwner, { detailResponse ->
            when (detailResponse) {

                is Resource.Success -> {
                    hideCustomDialog()
                    detailResponse.data?.let { tvShowDetail ->
                        binding.apply {
                            Glide.with(this@TvShowDetailsFragment)
                                .load("${Constance.POSTER_IMAGE_PATH_PREFIX}${tvShowDetail.poster_path}")
                                .fitCenter().error(R.drawable.ic_tv).into(posterImageView)
                            averageVoteTxt.text = tvShowDetail.vote_average.toString()
                            tagLine.text = tvShowDetail.tagline
                            spokenLanguages.text =
                                tvShowDetail.spoken_languages.joinToString { it.english_name }

                            if (tvShowDetail.overview.isNotEmpty()) {
                                tagDescription.text = tvShowDetail.overview
                            } else {
                                tagDescriptionError.visibility = View.VISIBLE
                            }


                        }
                    }
                }
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


