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
import com.example.e_commerce.model.pojo.draftorder.response.LineItem

class CartAdapter(
    private val onOperationClicked: (lineItems: List<LineItem>) -> Unit,
    private val onItemClick: (Long) -> Unit,
) : ListAdapter<LineItem, CartAdapter.CartViewHolder>(RecyclerDiffUtilCartItem()) {


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
        fun onBind(currentItem: LineItem) {
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

                btnPlus.setOnClickListener {
                    if (currentItem.properties[1].value.toInt() > currentItem.quantity + 1 && currentItem.quantity < 10) {
                        currentItem.quantity += 1
                        tvItemCount.text = currentItem.quantity.toString()
                         onOperationClicked(currentList)
                    } else {
                        Toast.makeText(
                            tvItemCount.context,
                            "Sorry you can't bay more",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                btnMinus.setOnClickListener {
                    if (currentItem.quantity > 1) {
                        currentItem.quantity -= 1
                        tvItemCount.text = currentItem.quantity.toString()
                        onOperationClicked(currentList)
                    }
                }

                binding.cvItem.setOnClickListener {
                    onItemClick(currentItem.id)
                }
            }
        }
    }
}

class RecyclerDiffUtilCartItem : DiffUtil.ItemCallback<LineItem>() {
    override fun areItemsTheSame(oldItem: LineItem, newItem: LineItem): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: LineItem, newItem: LineItem): Boolean {
        return oldItem == newItem
    }
}