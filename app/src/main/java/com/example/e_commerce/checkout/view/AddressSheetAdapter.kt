package com.example.e_commerce.checkout.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce.R
import com.example.e_commerce.databinding.BottomsheetchooseaddressBinding
import com.example.e_commerce.databinding.SheetaddressitemBinding
import com.example.e_commerce.model.pojo.customer_resposnse.Addresse

class AddressSheetAdapter(val onItemClick: (addresse: Addresse) -> Unit) :
    ListAdapter<Addresse, AddressSheetAdapter.AddressViewHolder>(RecyclerDiffUtilAddresse()) {
    private var selectedItemPosition: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = SheetaddressitemBinding.inflate(inflater, parent, false)
        return AddressViewHolder(binding)
    }


    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.onBind(currentItem, position)
    }

    inner class AddressViewHolder(private val binding: SheetaddressitemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun onBind(currentItem: Addresse, position: Int) {
            binding.tvUserAddress.text =
                "${currentItem.address1}, ${currentItem.city},\n${currentItem.country}"

            binding.cvAddr.setOnClickListener {
                val previousSelectedItemPosition = selectedItemPosition
                selectedItemPosition = position
                notifyItemChanged(previousSelectedItemPosition)
                notifyItemChanged(selectedItemPosition)
                onItemClick(currentItem)
            }

            if (selectedItemPosition == position) {
                binding.cvAddr.setCardBackgroundColor(
                    ContextCompat.getColor(binding.cvAddr.context, R.color.orange)
                )
            } else {
                binding.cvAddr.setCardBackgroundColor(
                    ContextCompat.getColor(binding.cvAddr.context, R.color.light_grey)
                )
            }
        }
    }
}

class RecyclerDiffUtilAddresse : DiffUtil.ItemCallback<Addresse>() {
    override fun areItemsTheSame(oldItem: Addresse, newItem: Addresse): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Addresse, newItem: Addresse): Boolean {
        return oldItem == newItem
    }
}