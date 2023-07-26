package com.example.e_commerce.orders.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce.R
import com.example.e_commerce.databinding.ProductListItemBinding
import com.example.e_commerce.model.pojo.customer_order_response.Order
import com.example.e_commerce.services.settingsharedpreference.SettingSharedPref
import com.example.e_commerce.utility.Constants


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
        @SuppressLint("SetTextI18n")
        fun onBind(currentItem: Order) {
            binding.apply {
                val settingSharedPref = SettingSharedPref.getInstance(tvItemName.context)
                val usdAmount = settingSharedPref.readStringFromSettingSP(Constants.USDAMOUNT)
                val currency = settingSharedPref.readStringFromSettingSP(Constants.CURRENCY)
                if (currency == Constants.EGP) {
                    tvItemPrice.text = "${currentItem.total_price} EGP"
                } else {
                    tvItemPrice.text = String.format(
                        "%.2f $",
                        currentItem.total_price.toDouble() * usdAmount.toDouble()
                    )
                }
                tvItemName.text = currentItem.created_at
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