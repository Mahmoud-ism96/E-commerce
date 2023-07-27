package com.example.e_commerce.checkout.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce.databinding.AddressItemBinding
import com.example.e_commerce.model.pojo.customer_resposnse.Addresse

class AddressesAdapter : ListAdapter<Addresse, AddressesAdapter.AddressesViewHolder>(RecyclerDiffUtilAddress()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressesViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = AddressItemBinding.inflate(inflater, parent, false)
        return AddressesViewHolder(binding)
    }


    override fun onBindViewHolder(holder: AddressesViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.onBind(currentItem)
    }

    inner class AddressesViewHolder(private val binding: AddressItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(currentItem: Addresse) {

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