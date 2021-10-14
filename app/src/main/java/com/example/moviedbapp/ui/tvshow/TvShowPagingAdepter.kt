package com.example.moviedbapp.ui.tvshow


import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviedbapp.R
import com.example.moviedbapp.databinding.ItemLayoutBinding
import com.example.moviedbapp.modal.tvshow.TvShowList

import com.example.moviedbapp.utiles.Constance.POSTER_IMAGE_PATH_PREFIX

class TvShowPagingAdepter(private val listener: OnItemClickListenerForTv) :
    PagingDataAdapter<TvShowList, TvShowPagingAdepter.ViewHolder>(TvShowDiffUtilCallBack()) {


    inner class ViewHolder(private val binding: ItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {


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

        fun bind(item: TvShowList) {


            Glide.with(binding.root.context)
                .load("${POSTER_IMAGE_PATH_PREFIX}${item.poster_path}")
                .fitCenter()
                .error(R.drawable.ic_tv)
                .into(binding.moviePosterImageView)

            binding.tvName.text = item.name

        }
    }


    class TvShowDiffUtilCallBack : DiffUtil.ItemCallback<TvShowList>() {
        override fun areItemsTheSame(oldItem: TvShowList, newItem: TvShowList): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TvShowList, newItem: TvShowList): Boolean {
            return oldItem == newItem
        }
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding =
            ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    interface OnItemClickListenerForTv {
        fun onItemClick(tvShowList: TvShowList)

    }

}