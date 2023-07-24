package com.example.e_commerce.checkout.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.model.pojo.level.InventoryLevelData
import com.example.e_commerce.model.pojo.order.OrderData
import com.example.e_commerce.model.repo.RepoInterface
import com.example.e_commerce.services.network.ApiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CheckOutViewModel(private val repo: RepoInterface) : ViewModel() {

    fun createOrder(order: OrderData) {
        viewModelScope.launch {
            repo.createOrder(order)
        }
    }

    fun updateVariantQuantity(inventoryLevelData: InventoryLevelData){
        viewModelScope.launch {
            repo.updateInventoryLevel(inventoryLevelData)
        }
    }
}