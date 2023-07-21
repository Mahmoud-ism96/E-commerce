package com.example.e_commerce.shoppingcart.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.e_commerce.R
import com.example.e_commerce.databinding.CartItemBinding
import com.example.e_commerce.model.pojo.CartItem

class CartAdapter(
    private val onPlusClick: (Long, Int) -> Unit,
    private val onMinusClick: (Long, Int) -> Unit,
    private val onItemClick: (Long) -> Unit,
) : ListAdapter<CartItem, CartAdapter.CartViewHolder>(RecyclerDiffUtilCartItem()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = CartItemBinding.inflate(inflater, parent, false)
        return CartViewHolder(binding)
    }


    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.onBind(currentItem)
    }

    inner class CartViewHolder(private val binding: CartItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(currentItem: CartItem) {
            binding.apply {
                tvItemName.text = currentItem.title
                tvItemPrice.text = currentItem.price.toString()
                tvItemCount.text = currentItem.quantity.toString()
                Glide.with(ivItemImage.context)
                    .load(currentItem.imageSrc)
                    .apply(RequestOptions().override(200, 200))
                    .placeholder(R.drawable.loading_svgrepo_com)
                    .error(R.drawable.error)
                    .into(ivItemImage)

                btnPlus.setOnClickListener {
                    if (currentItem.inventoryQuantity > currentItem.quantity + 1) {
                        onPlusClick(currentItem.id, currentItem.quantity)
                        val newQuantity = currentItem.quantity + 1
                        tvItemCount.text = newQuantity.toString()
                    }else{
                        Toast.makeText(tvItemCount.context, "Sorry you can't bay more", Toast.LENGTH_SHORT).show()
                    }
                }
                btnMinus.setOnClickListener { onMinusClick(currentItem.id, currentItem.quantity) }

                binding.cvItem.setOnClickListener {
                    onItemClick(currentItem.id)
                }
            }
        }
    }
}

class RecyclerDiffUtilCartItem : DiffUtil.ItemCallback<CartItem>() {
    override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
        return oldItem == newItem
    }
}