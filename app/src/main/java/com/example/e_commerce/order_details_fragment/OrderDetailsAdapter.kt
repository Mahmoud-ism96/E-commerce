package com.example.e_commerce.order_details_fragment

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
import com.example.e_commerce.model.pojo.order_details_response.LineItem
import com.example.e_commerce.services.network.ConcreteRemoteSource
import com.example.e_commerce.services.settingsharedpreference.SettingSharedPref
import com.example.e_commerce.utility.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OrderDetailsAdapter(private val onClick: (Long) -> Unit) :
    ListAdapter<LineItem, OrderDetailsAdapter.orderViewHolder>(RecyclerDiffUtilOrder()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): orderViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = ProductListItemBinding.inflate(inflater, parent, false)
        return orderViewHolder(binding)
    }


    override fun onBindViewHolder(holder: orderViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.onBind(currentItem)
    }

    inner class orderViewHolder(private val binding: ProductListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun onBind(currentItem: LineItem) {
            binding.apply {
                val settingSharedPref = SettingSharedPref.getInstance(tvItemName.context)
                val usdAmount = settingSharedPref.readStringFromSettingSP(Constants.USDAMOUNT)
                val currency = settingSharedPref.readStringFromSettingSP(Constants.CURRENCY)
                if (currency == Constants.EGP) {
                    tvItemPrice.text = "${currentItem.price} EGP"
                } else {
                    tvItemPrice.text = String.format(
                        "%.2f $",
                        currentItem.price.toDouble() * usdAmount.toDouble()
                    )
                }
                tvItemName.text = currentItem.title
                try {
                    CoroutineScope(Dispatchers.IO).launch {
                        ConcreteRemoteSource.getProductById(currentItem.product_id).collectLatest {
                            if (it.isSuccessful) {
                                withContext(Dispatchers.Main) {
                                    Glide.with(ivItemImage.context)
                                        .load(it.body()!!.product.image.src)
                                        .apply(RequestOptions().override(200, 200))
                                        .placeholder(R.drawable.loading_svgrepo_com)
                                        .error(R.drawable.error)
                                        .into(ivItemImage)
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    CoroutineScope(Dispatchers.IO).cancel()
                }
                item.setOnClickListener {
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