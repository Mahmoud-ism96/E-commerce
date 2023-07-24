package com.example.e_commerce.product_details.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce.R
import com.example.e_commerce.databinding.ItemSizeBinding
import com.example.e_commerce.model.pojo.product_details.Variant

class SizeAdapter(private val onClick: (Long) -> Unit) :
    ListAdapter<Variant, SizeAdapter.SizeViewHolder>(RecyclerDiffUtilSize()) {

    private var selectedItemPosition: Int = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SizeViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = ItemSizeBinding.inflate(inflater, parent, false)
        return SizeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SizeViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.onBind(currentItem, position)
    }

    inner class SizeViewHolder(private val binding: ItemSizeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(currentItem: Variant, position: Int) {
            if (!currentItem.option1.isNullOrBlank()) {
                binding.tvVariantSize.text = currentItem.option1

                // Set background color based on selected position
                if (selectedItemPosition == position) {
                    binding.cvVariantSize.setCardBackgroundColor(
                        ContextCompat.getColor(binding.cvVariantSize.context, R.color.orange)
                    )
                } else {
                    binding.cvVariantSize.setCardBackgroundColor(
                        ContextCompat.getColor(binding.cvVariantSize.context, R.color.light_grey)
                    )
                }

                binding.cvVariantSize.setOnClickListener {
                    // Save the selected position and update the UI
                    val previousSelectedItemPosition = selectedItemPosition
                    selectedItemPosition = position
                    notifyItemChanged(previousSelectedItemPosition)
                    notifyItemChanged(selectedItemPosition)

                    onClick(currentItem.id)
                }
            }
        }
    }
}

class RecyclerDiffUtilSize : DiffUtil.ItemCallback<Variant>() {
    override fun areItemsTheSame(oldItem: Variant, newItem: Variant): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Variant, newItem: Variant): Boolean {
        return oldItem == newItem
    }
}
