package com.example.moviedbapp.ui.movies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviedbapp.databinding.ItemLayoutBinding
import com.example.moviedbapp.modal.movie.MovieList
import com.example.moviedbapp.modal.tvshow.TvShowList

import com.example.moviedbapp.utiles.Constance.POSTER_IMAGE_PATH_PREFIX

class MoviePagingAdepter(private val listener: OnItemClickListenerForMovie) : PagingDataAdapter<MovieList, MoviePagingAdepter.ViewHolder>(
    MovieDiffUtilCallBack()
) {


    inner class ViewHolder(private val binding: ItemLayoutBinding): RecyclerView.ViewHolder(binding.root){

        init {
            binding.root.setOnClickListener {

                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null) {
                        listener.onItemClick(item)
                    }
                }
            }
        }

        fun bind(item: MovieList ){

            binding.tvName.text = item.title
            Glide.with(binding.root.context)
                .load("${POSTER_IMAGE_PATH_PREFIX}${item.poster_path}")
                .fitCenter()
                .into(binding.moviePosterImageView)
         }
    }


    class MovieDiffUtilCallBack : DiffUtil.ItemCallback<MovieList>() {
        override fun areItemsTheSame(oldItem: MovieList, newItem: MovieList): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MovieList, newItem: MovieList): Boolean {
            return oldItem == newItem
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    interface OnItemClickListenerForMovie {
        fun onItemClick(movieList: MovieList)

    }
}