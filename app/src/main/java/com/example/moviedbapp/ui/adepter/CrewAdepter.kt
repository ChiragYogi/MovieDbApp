package com.example.moviedbapp.ui.adepter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviedbapp.R
import com.example.moviedbapp.databinding.CrewItemBinding
import com.example.moviedbapp.modal.cast.Crew
import com.example.moviedbapp.utiles.Constance

class CrewAdepter(): RecyclerView.Adapter<CrewAdepter.ViewHolder>() {

    private var list: List<Crew> = ArrayList()
    class ViewHolder(private val binding: CrewItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Crew){
            with(binding){
                Glide.with(binding.root.context)
                    .load("${Constance.POSTER_IMAGE_PATH_PREFIX}${item.profile_path}")
                    .circleCrop()
                    .error(R.drawable.ic_baseline_account_circle_24).into(crewImage)
                crewNameTxt.text = item.original_name
                crewJobNameTxt.text = item.department
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CrewItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
       return list.size
    }

    fun swapData(list: List<Crew>){
        this.list = list
        notifyDataSetChanged()
    }
}