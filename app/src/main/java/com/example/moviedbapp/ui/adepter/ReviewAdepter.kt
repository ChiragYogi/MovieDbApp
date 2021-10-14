package com.example.moviedbapp.ui.adepter

import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.moviedbapp.R
import com.example.moviedbapp.databinding.ReviewItemBinding
import com.example.moviedbapp.modal.cast.Crew
import com.example.moviedbapp.modal.review.Review
import com.example.moviedbapp.utiles.Constance


class ReviewAdepter(): RecyclerView.Adapter<ReviewAdepter.ViewHolder>()  {

    private var list: List<Review> = ArrayList()
    class ViewHolder(private val binding: ReviewItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Review){
            with(binding){
                val url = "${Constance.POSTER_IMAGE_PATH_PREFIX}${item.author_details.avatar_path}"
                   Glide.with(root.context)
                        .load(url)
                        .placeholder(R.drawable.ic_baseline_account_circle_24)
                        .fallback(R.drawable.ic_baseline_account_circle_24)
                        .transform(CircleCrop())
                        .error(R.drawable.ic_baseline_account_circle_24).into(authorImage)

               authorName.text = item.author_details.name
                authorReviewScore.text = item.author_details.rating.toString()

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    reviewText.text = Html.fromHtml(
                        item.content, Html.FROM_HTML_MODE_COMPACT
                    )
                } else {
                    reviewText.text = Html.fromHtml(item.content)
                }



            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ReviewItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = list[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
       return list.size
    }

    fun swapData(list: List<Review>){
        this.list = list
        notifyDataSetChanged()
    }
}