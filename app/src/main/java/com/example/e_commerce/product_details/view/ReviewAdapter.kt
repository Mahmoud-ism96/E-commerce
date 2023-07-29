package com.example.e_commerce.product_details.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce.databinding.ReviewItemBinding
import com.example.e_commerce.model.pojo.Review

class ReviewAdapter :
    ListAdapter<Review, ReviewAdapter.ReviewViewHolder>(RecyclerDiffUtilReview()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = ReviewItemBinding.inflate(inflater, parent, false)
        return ReviewViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.onBind(currentItem)
    }

    inner class ReviewViewHolder(private val binding: ReviewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(currentItem: Review) {
            if (!currentItem.reviewDetails.isNullOrBlank())
                binding.apply {
                    rbReview.rating = currentItem.rating
                    tvReviewDetails.text = currentItem.reviewDetails
                    tvReviewName.text = currentItem.reviewerName
                    tvReviewDate.text = currentItem.reviewDate

                }
        }
    }
}

class RecyclerDiffUtilReview : DiffUtil.ItemCallback<Review>() {
    override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean {
        return oldItem == newItem
    }
}