package com.example.e_commerce.search.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce.databinding.ItemHintBinding
import com.example.e_commerce.model.pojo.Brand

class ProductHintAdapter(private val onClick: (String) -> Unit) :
    ListAdapter<Brand, ProductHintAdapter.ProductHintViewHolder>(
        RecyclerDiffUtilBrand()
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHintViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = ItemHintBinding.inflate(inflater, parent, false)
        return ProductHintViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductHintViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.onBind(currentItem)
    }

    inner class ProductHintViewHolder(private val binding: ItemHintBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(currentItem: Brand) {
            binding.tvBrandHint.text = currentItem.body_html

            binding.tvBrandHint.setOnClickListener {
                onClick(currentItem.title)
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