package com.example.moviedbapp.ui.tvshow

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.moviedbapp.databinding.LoadStateBinding

class LoadStateAdepter(private val retry: () -> Unit): LoadStateAdapter<LoadStateAdepter.ViewHolder>() {


    inner class ViewHolder(private val binding: LoadStateBinding): RecyclerView.ViewHolder(binding.root) {

        init {
            binding.retryButton.setOnClickListener { retry.invoke() }
        }

        fun bind(loadState: LoadState){

            if(loadState is LoadState.Error){
                binding.errorMsg.text = loadState.error.localizedMessage
            }

            binding.apply {
                progressBar.isVisible = loadState is LoadState.Loading
                retryButton.isVisible = loadState !is LoadState.Loading
                errorMsg.isVisible = loadState !is LoadState.Loading
            }

        }
    }

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        val binding =
            LoadStateBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }
}