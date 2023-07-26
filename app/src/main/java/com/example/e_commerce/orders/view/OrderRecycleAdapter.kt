package com.example.e_commerce.orders.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce.R
import com.example.e_commerce.databinding.ProductListItemBinding
import com.example.e_commerce.model.pojo.customer_order_response.Order


class OrderRecycleAdapter(private val onClick: (Long) -> Unit) :
    ListAdapter<Order, OrderRecycleAdapter.OrderViewHolder>(RecyclerDiffUtilOrder()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = ProductListItemBinding.inflate(inflater, parent, false)
        return OrderViewHolder(binding)
    }


    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.onBind(currentItem)
    }

    inner class OrderViewHolder(private val binding: ProductListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(currentItem: Order) {
            binding.apply {
                tvItemName.text = currentItem.created_at
                tvItemPrice.text = currentItem.total_price
                ivItemImage.setImageResource(R.drawable.shop)
                item.setOnClickListener {
                    onClick(currentItem.id)
                }
            }
        }
    }
}

class RecyclerDiffUtilOrder : DiffUtil.ItemCallback<Order>() {
    override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
        return oldItem == newItem
    }
}