package com.example.e_commerce.product_details.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.model.pojo.CartItem
import com.example.e_commerce.model.repo.RepoInterface
import com.example.e_commerce.services.network.ApiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ProductDetailsViewModel(private val repo: RepoInterface) : ViewModel() {
    private val _productDetailsMutableState: MutableStateFlow<ApiState> = MutableStateFlow(ApiState.Loading)

    val productDetailsMutableState: StateFlow<ApiState> get() = _productDetailsMutableState

    fun addToCart(cartItem : CartItem){
        viewModelScope.launch {
            repo.insertItem(cartItem)
        }
    }

    fun getProductDetails(productID: Long) {
        viewModelScope.launch {
            repo.getProductById(productID).catch { _productDetailsMutableState.value = ApiState.Failure(it) }
                .collect {
                    if (it.isSuccessful) {
                        _productDetailsMutableState.value = ApiState.Success(it.body()!!)
                    }
                }
        }
    }

}