package com.example.moviedbapp.ui.adepter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.moviedbapp.R
import com.example.moviedbapp.databinding.CastItemBinding
import com.example.moviedbapp.modal.cast.Cast
import com.example.moviedbapp.modal.cast.CastResponse
import com.example.moviedbapp.utiles.Constance

class CastAdepter():RecyclerView.Adapter<CastAdepter.ViewHolder>() {

    private var list: List<Cast> = ArrayList()
    class ViewHolder(private val binding: CastItemBinding):RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Cast){
            with(binding){

                Glide.with(binding.root.context)
                    .load("${Constance.POSTER_IMAGE_PATH_PREFIX}${item.profile_path}")
                    .placeholder(R.drawable.ic_baseline_account_circle_24)
                    .fallback(R.drawable.ic_baseline_account_circle_24)
                    .transform(CircleCrop())
                    .error(R.drawable.ic_baseline_account_circle_24)
                    .into(castImage)
                starNameTxt.text = item.original_name
                charecterNameTxt.text = item.character
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
      val binding = CastItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val item = list[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun swapData(list: List<Cast>){
       this.list = list
        notifyDataSetChanged()
    }
}