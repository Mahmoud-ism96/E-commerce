package com.example.e_commerce.checkout.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.e_commerce.R
import com.example.e_commerce.databinding.CartItemBinding
import com.example.e_commerce.model.pojo.draftorder.response.LineItem

class CheckOutAdapter:
    ListAdapter<LineItem, CheckOutAdapter.CheckViewHolder>(RecyclerDiffUtilLineItem()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = CartItemBinding.inflate(inflater, parent, false)
        return CheckViewHolder(binding)
    }


    override fun onBindViewHolder(holder: CheckViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.onBind(currentItem)
    }

    inner class CheckViewHolder(private val binding: CartItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(currentItem: LineItem) {
            binding.btnMinus.visibility = View.GONE
            binding.btnPlus.visibility = View.GONE
            binding.apply {
                tvItemName.text = currentItem.title
                tvItemPrice.text = currentItem.price
                tvItemCount.text = currentItem.quantity.toString()
                Glide.with(ivItemImage.context)
                    .load(currentItem.properties[0].value)
                    .apply(RequestOptions().override(200, 200))
                    .placeholder(R.drawable.loading_svgrepo_com)
                    .error(R.drawable.error)
                    .into(ivItemImage)
                binding.tvItemCount.text = currentItem.quantity.toString()

            }
        }
    }
}

class RecyclerDiffUtilLineItem : DiffUtil.ItemCallback<LineItem>() {
    override fun areItemsTheSame(oldItem: LineItem, newItem: LineItem): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: LineItem, newItem: LineItem): Boolean {
        return oldItem == newItem
    }
}
