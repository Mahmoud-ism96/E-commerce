package com.example.e_commerce.home.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce.R
import com.example.e_commerce.databinding.OfferListItemBinding
import com.example.e_commerce.model.pojo.Ad
import com.example.e_commerce.utility.Constants

class CouponsRecyclerAdapter(private val onImageClick: (String)-> Unit) : ListAdapter<Ad, CouponsRecyclerAdapter.CouponViewHolder>(RecyclerDiffUtilAd()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CouponViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = OfferListItemBinding.inflate(inflater, parent, false)
        return CouponViewHolder(binding)
    }


    override fun onBindViewHolder(holder: CouponViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.onBind(currentItem)
    }

    inner class CouponViewHolder(private val binding: OfferListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(currentItem: Ad) {
            binding.apply {
                when(currentItem.id){
                    Constants.PRICE_RULE_ID_1.toLong() -> binding.imageView4.setImageResource(R.drawable.ad1)
                    Constants.PRICE_RULE_ID_2.toLong() -> binding.imageView4.setImageResource(R.drawable.ad2)
                }

                binding.cvOffer.setOnClickListener {
                    onImageClick(currentItem.title)
                }
            }
        }
    }
}

class RecyclerDiffUtilAd : DiffUtil.ItemCallback<Ad>() {
    override fun areItemsTheSame(oldItem: Ad, newItem: Ad): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Ad, newItem: Ad): Boolean {
        return oldItem == newItem
    }
}