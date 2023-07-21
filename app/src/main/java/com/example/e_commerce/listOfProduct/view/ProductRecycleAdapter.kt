package com.example.e_commerce.listOfProduct.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.e_commerce.R
import com.example.e_commerce.databinding.ProductListItemBinding
import com.example.e_commerce.model.pojo.Product

class ProductRecycleAdapter(private val onClick: (Product) -> Unit) :
    ListAdapter<Product, ProductRecycleAdapter.ProductViewHolder>(RecyclerDiffUtilProduct()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = ProductListItemBinding.inflate(inflater, parent, false)
        return ProductViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.onBind(currentItem)
    }

    inner class ProductViewHolder(private val binding: ProductListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun onBind(currentItem: Product) {
            binding.apply {
                tvItemName.text = currentItem.title
                tvItemPrice.text = "${currentItem.variants[0].price} EGP"
                Glide.with(tvItemName.context)
                    .load(currentItem.image.src)
                    .apply(RequestOptions().override(200, 200))
                    .placeholder(R.drawable.loading_svgrepo_com)
                    .error(R.drawable.error)
                    .into(ivItemImage)
                item.setOnClickListener {
                    onClick(currentItem)
                }
            }
        }
    }
}

class RecyclerDiffUtilProduct : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem
    }
}