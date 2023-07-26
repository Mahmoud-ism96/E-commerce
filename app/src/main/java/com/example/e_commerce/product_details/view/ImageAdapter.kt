package com.example.e_commerce.product_details.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.e_commerce.R
import com.example.e_commerce.databinding.ItemImagesBinding
import com.example.e_commerce.model.pojo.Image

class ImageAdapter : ListAdapter<Image, ImageAdapter.ImageViewHolder>(RecyclerDiffUtilImage()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = ItemImagesBinding.inflate(inflater, parent, false)
        return ImageViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.onBind(currentItem)
    }

    inner class ImageViewHolder(private val binding: ItemImagesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(currentItem: Image) {
            Glide.with(binding.ivDetailImage.context)
                .load(currentItem.src)
                .apply(RequestOptions().override(200, 200))
                .placeholder(R.drawable.loading_svgrepo_com)
                .error(R.drawable.error)
                .into(binding.ivDetailImage)


        }
    }
}

class RecyclerDiffUtilImage : DiffUtil.ItemCallback<Image>() {
    override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean {
        return oldItem == newItem
    }
}