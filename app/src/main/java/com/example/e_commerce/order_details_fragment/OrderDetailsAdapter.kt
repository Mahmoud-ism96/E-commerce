package com.example.e_commerce.order_details_fragment

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce.databinding.OrderDetailsListItemBinding
import com.example.e_commerce.model.pojo.order_details_response.LineItem

class OrderDetailsAdapter(private val onClick: (Long) -> Unit) : ListAdapter<LineItem, OrderDetailsAdapter.orderViewHolder>(RecyclerDiffUtilOrder()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): orderViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = OrderDetailsListItemBinding.inflate(inflater, parent, false)
        return orderViewHolder(binding)
    }


    override fun onBindViewHolder(holder: orderViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.onBind(currentItem)
    }

    inner class orderViewHolder(private val binding: OrderDetailsListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(currentItem: LineItem) {
            binding.apply {
                tvProductName.text=currentItem.title
                tvProductPrice.text=currentItem.price
                cvOrderItem.setOnClickListener {
                    onClick(currentItem.product_id)
                }
            }
        }
    }
}

class RecyclerDiffUtilOrder : DiffUtil.ItemCallback<LineItem>() {
    override fun areItemsTheSame(oldItem: LineItem, newItem: LineItem): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: LineItem, newItem: LineItem): Boolean {
        return oldItem == newItem
    }
}