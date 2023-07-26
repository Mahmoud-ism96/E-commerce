package com.example.e_commerce.wishlist.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.e_commerce.R
import com.example.e_commerce.databinding.ItemProductSwipeBinding
import com.example.e_commerce.model.pojo.draftorder.response.LineItem
import com.example.e_commerce.services.settingsharedpreference.SettingSharedPref
import com.example.e_commerce.utility.Constants

class WishListAdapter(private val onClick: (Long) -> Unit) :
    ListAdapter<LineItem, WishListAdapter.WishlistViewHolder>(RecyclerDiffUtilWishlistItem()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishlistViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = ItemProductSwipeBinding.inflate(inflater, parent, false)
        return WishlistViewHolder(binding)
    }


    override fun onBindViewHolder(holder: WishlistViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.onBind(currentItem)
    }

    inner class WishlistViewHolder(private val binding: ItemProductSwipeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(currentItem: LineItem) {
            binding.apply {
                tvSwipeItemName.text = currentItem.title
                val settingSharedPref = SettingSharedPref.getInstance(tvSwipeItemName.context)
                val usdAmount = settingSharedPref.readStringFromSettingSP(Constants.USDAMOUNT)
                val currency = settingSharedPref.readStringFromSettingSP(Constants.CURRENCY)
                if (currency == Constants.USD) {
                    tvSwipeItemPrice.text= String.format("%.2f $",currentItem.price.toDouble()*usdAmount.toDouble())
                } else {
                    tvSwipeItemPrice.text = "${currentItem.price} EGP"
                }
                Glide.with(tvSwipeItemName.context).load(currentItem.properties[0].value)
                    .apply(RequestOptions().override(200, 200))
                    .placeholder(R.drawable.loading_svgrepo_com).error(R.drawable.error)
                    .into(ivSwipeItemImage)
                swipeItem.setOnClickListener {
                    onClick(currentItem.product_id)
                }
            }
        }
    }
}

class RecyclerDiffUtilWishlistItem : DiffUtil.ItemCallback<LineItem>() {
    override fun areItemsTheSame(oldItem: LineItem, newItem: LineItem): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: LineItem, newItem: LineItem): Boolean {
        return oldItem == newItem
    }
}