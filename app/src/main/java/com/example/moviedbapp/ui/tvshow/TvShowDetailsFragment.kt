package com.example.moviedbapp.ui.tvshow

import android.app.Dialog
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
import com.example.moviedbapp.databinding.FragmentTvshowDetailesBinding
import com.example.moviedbapp.modal.tvshow.TvShowResponseWithAllDetail

import com.example.moviedbapp.ui.adepter.CastAdepter
import com.example.moviedbapp.ui.adepter.CrewAdepter
import com.example.moviedbapp.ui.adepter.ReviewAdepter
import com.example.moviedbapp.utiles.Constance
import com.example.moviedbapp.utiles.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TvShowDetailsFragment : Fragment(R.layout.fragment_tvshow_detailes) {

    private var _binding: FragmentTvshowDetailesBinding? = null
    private val binding get() = _binding!!
    private var mProgressDialog: Dialog? = null

    private val mViewModel by viewModels<TvShowDetailViewModel>()
    private val args: TvShowDetailsFragmentArgs by navArgs()
    private val castAdepter = CastAdepter()
    private val crewAdepter = CrewAdepter()
    private val reviewAdepter = ReviewAdepter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeDetailResponse()
    }

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

        setUpCastRecyclerView()
        setUpCrewRecyclerView()
        setUpReviewRecyclerView()

    }

    private fun observeDetailResponse() {
        mViewModel.tvDetails.observe(this) { detailsResponse ->
            when (detailsResponse) {
                is Resource.Error -> {
                    showErrorsAndHideView(
                        detailsResponse.message ?: getString(R.string.error_message)
                    )
                }
                is Resource.Loading -> {
                    showCustomDialog()
                }
                is Resource.Success -> {
                    setDataInUi(detailsResponse.data)
                }
            }
        }


    }

    private fun setDataInUi(data: TvShowResponseWithAllDetail?) {
            hideCustomDialog()
        data?.castResponse?.let { response ->
            // cast crew  data
            if (response.cast.isNotEmpty()) {
                castAdepter.swapData(response.cast)
            } else {
                binding.noCastErrorTxt.visibility = View.VISIBLE
            }
            if (response.crew.isNotEmpty()) {
                crewAdepter.swapData(response.crew)
            } else {
                binding.noCrewErrorTxt.visibility = View.VISIBLE
            }
        }

        data?.reviewResponse?.let { reviewResponse ->

            if (reviewResponse.total_results > 0) {
                reviewAdepter.swapData(reviewResponse.results)
            } else {
                binding.reviewRvView.visibility = View.GONE
                binding.noReviewErrorTxt.visibility = View.VISIBLE
            }
        }

        data?.tvShowDetailResponse?.let { tvShowDetailResponse ->
            //detail data
            Glide.with(this@TvShowDetailsFragment)
                .load("${Constance.POSTER_IMAGE_PATH_PREFIX}${tvShowDetailResponse.poster_path}")
                .fitCenter().error(R.drawable.ic_tv).into(binding.posterImageView)
            binding.averageVoteTxt.text = tvShowDetailResponse.vote_average.toString()
            binding.tagLine.text = tvShowDetailResponse.tagline
            binding.spokenLanguages.text =
                tvShowDetailResponse.spoken_languages.joinToString { it.english_name }

            if (tvShowDetailResponse.overview.isNotEmpty()) {
                binding.tagDescription.text = tvShowDetailResponse.overview
            } else {
                binding.tagDescriptionError.visibility = View.VISIBLE
            }

        }


    }


    private fun showErrorsAndHideView(message: String) {
        hideCustomDialog()
        binding.apply {
            noCastErrorTxt.visibility = View.VISIBLE
            noCrewErrorTxt.visibility = View.VISIBLE
            noReviewErrorTxt.visibility = View.VISIBLE
            binding.tagDescriptionError.visibility = View.VISIBLE
            binding.tagDescriptionError.text = message
            noReviewErrorTxt.text = message
            noCastErrorTxt.text = message
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


