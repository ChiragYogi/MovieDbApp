package com.example.moviedbapp.ui.tvshow

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.example.moviedbapp.R
import com.example.moviedbapp.databinding.FragmentTvShowBinding
import com.example.moviedbapp.modal.tvshow.TvShowList
import com.example.moviedbapp.ui.viewmodel.PagingDataViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TvShowFragment : Fragment(R.layout.fragment_tv_show),
    TvShowPagingAdepter.OnItemClickListenerForTv {

    private var _binding: FragmentTvShowBinding? = null
    private val binding get() = _binding!!
    private val mViewModel: PagingDataViewModel by viewModels()
    private val mAdepter = TvShowPagingAdepter(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentTvShowBinding.bind(view)
        setUpRecyclerView()
        setUpObserver()
        loadStateListener()

        binding.retryButton.setOnClickListener {

            mAdepter.retry()
        }
    }

    private fun setUpObserver() {
        mViewModel.tvShowList.observe(viewLifecycleOwner) { tvShowData ->
            mAdepter.submitData(lifecycle, tvShowData)

        }


    }

    private fun loadStateListener(){

        mAdepter.addLoadStateListener { loadState ->

            binding.apply {
                showProgressBar.isVisible = loadState.source.refresh is LoadState.Loading
                showProgressText.isVisible = loadState.source.refresh is LoadState.Loading
                if (loadState.source.refresh is LoadState.Error){
                    rvView.visibility = View.INVISIBLE
                }
                rvView.isVisible = loadState.source.refresh is LoadState.NotLoading


                retryButton.isVisible = loadState.source.refresh is LoadState.Error
                errorMsg.isVisible = loadState.source.refresh is LoadState.Error

                if (loadState.source.refresh is LoadState.NotLoading &&
                        loadState.append.endOfPaginationReached &&
                    mAdepter.itemCount < 1){
                    rvView.isVisible = false
                    errorMsg.isVisible = true

                }else{
                    errorMsg.isVisible = false
                }
            }

        }
    }

    private fun setUpRecyclerView() {

        binding.rvView.apply {
            layoutManager = GridLayoutManager(context,2)
            adapter = mAdepter
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL))

        }
    }

    override fun onItemClick(tvShowList: TvShowList) {
     val action = TvShowFragmentDirections.actionNavigationTvShowToTvShowDetailsFragment(tvShowList)
        findNavController().navigate(action)
    }


}


