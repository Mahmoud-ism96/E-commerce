package com.example.e_commerce.profile.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce.databinding.AddressItemBinding
import com.example.e_commerce.model.pojo.customer_resposnse.Addresse

class AddressRecyclerAdapter (private val onSetDefaultClick: (Long, Long) -> Unit) :
    ListAdapter<Addresse, AddressRecyclerAdapter.AddressViewHolder>(RecyclerDiffUtilAddress()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = AddressItemBinding.inflate(inflater, parent, false)
        return AddressViewHolder(binding)
    }


    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.onBind(currentItem)
    }

    inner class AddressViewHolder(private val binding: AddressItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun onBind(currentItem: Addresse) {
            binding.apply {
                tvName.text = "${currentItem.first_name}  ${currentItem.last_name}"
                tvAddress.text = currentItem.address1
                tvAdditionalInfo.text = currentItem.address2.toString()
                tvCity.text = currentItem.city
                tvPhone.text = currentItem.phone
                tvRegion.text = currentItem.country_name
                if(currentItem.default){
                    tvDefaultAddress.visibility = View.VISIBLE
                    btnSetDefault.visibility = View.GONE
                }else{
                    tvDefaultAddress.visibility = View.GONE
                    btnSetDefault.visibility = View.VISIBLE
                }

                btnSetDefault.setOnClickListener {
                    btnSetDefault.visibility = View.GONE
                    onSetDefaultClick(currentItem.customer_id, currentItem.id)
                }

            }
        }
    }
}

class RecyclerDiffUtilAddress : DiffUtil.ItemCallback<Addresse>() {
    override fun areItemsTheSame(oldItem: Addresse, newItem: Addresse): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Addresse, newItem: Addresse): Boolean {
        return oldItem == newItem
    }
}