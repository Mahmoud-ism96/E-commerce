package com.example.e_commerce.Home.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.e_commerce.R
import com.example.e_commerce.databinding.CarouselLayoutBinding
import com.example.e_commerce.model.pojo.Brand

class BrandRecycleAdapter(private val onClick: (Brand) -> Unit) : ListAdapter<Brand, BrandRecycleAdapter.BrandViewHolder>(RecyclerDiffUtilBrand()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = CarouselLayoutBinding.inflate(inflater, parent, false)
        return BrandViewHolder(binding)
    }


    override fun onBindViewHolder(holder: BrandViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.onBind(currentItem)
    }

    inner class BrandViewHolder(private val binding: CarouselLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(currentItem: Brand) {
            binding.apply {
                tvBrand.text=currentItem.title
                (Glide.with(tvBrand.context)
                    .load(currentItem.image.src)
                    .apply(RequestOptions().override(200, 200))
                    .placeholder(R.drawable.loading_svgrepo_com)
                    .error(R.drawable.error)
                    .into(ivBrand))
                cvBrand.setOnClickListener {
                    onClick(currentItem)
                }
            }
        }
    }
}

class RecyclerDiffUtilBrand : DiffUtil.ItemCallback<Brand>() {
    override fun areItemsTheSame(oldItem: Brand, newItem: Brand): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Brand, newItem: Brand): Boolean {
        return oldItem == newItem
    }
}