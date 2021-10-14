package com.example.moviedbapp.ui.movies

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.example.moviedbapp.R
import com.example.moviedbapp.TMDBApplication
import com.example.moviedbapp.databinding.FragmentMovieBinding
import com.example.moviedbapp.modal.movie.MovieList
import com.example.moviedbapp.ui.viewmodel.PagingDataViewModel
import com.example.moviedbapp.ui.viewmodel.TMDBViewModelProvider
import kotlinx.coroutines.launch


class MovieFragment : Fragment(R.layout.fragment_movie),
    MoviePagingAdepter.OnItemClickListenerForMovie {

    private var _binding: FragmentMovieBinding? = null
    private val binding get() = _binding!!
    private val mViewModel: PagingDataViewModel by viewModels {
        TMDBViewModelProvider((activity?.application as TMDBApplication).repo)
    }
    private val mAdepter = MoviePagingAdepter(this)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMovieBinding.bind(view)

        setUpRecyclerView()
        setUpObserver()
        loadStateListener()


    }

    private fun loadStateListener() {
        mAdepter.addLoadStateListener { loadState ->

            binding.apply {
                showProgressBar.isVisible = loadState.source.refresh is LoadState.Loading
                showProgressText.isVisible = loadState.source.refresh is LoadState.Loading

                if (loadState.source.refresh is LoadState.Error) {
                    rvView.visibility = View.INVISIBLE
                }

                rvView.isVisible = loadState.source.refresh is LoadState.NotLoading
                retryButton.isVisible = loadState.source.refresh is LoadState.Error
                errorMsg.isVisible = loadState.source.refresh is LoadState.Error

                if (loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached && mAdepter.itemCount < 1
                ) {
                    rvView.isVisible = false
                    errorMsg.isVisible = true

                } else {
                    errorMsg.isVisible = false
                }
            }

        }
    }

    private fun setUpObserver() {
        mViewModel.movieList.observe(viewLifecycleOwner, {
            mAdepter.submitData(lifecycle, it)
        })


    }


    private fun setUpRecyclerView() {
        val decorationVertical = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        val decorationHorizontal = DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL)
        binding.rvView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = mAdepter
            setHasFixedSize(true)
            addItemDecoration(decorationVertical)
            addItemDecoration(decorationHorizontal)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(movieList: MovieList) {
        val action = MovieFragmentDirections.actionNavigationMovieToMovieDetailFragment(movieList)
        findNavController().navigate(action)
    }


}