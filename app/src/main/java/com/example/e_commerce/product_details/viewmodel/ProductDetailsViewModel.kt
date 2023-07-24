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
    private val _productDetailsState: MutableStateFlow<ApiState> = MutableStateFlow(ApiState.Loading)

    val productDetailsState: StateFlow<ApiState> get() = _productDetailsState

    fun addToCart(cartItem : CartItem){
        viewModelScope.launch {
            repo.insertItem(cartItem)
        }
    }

    fun getProductDetails(productID: Long) {
        viewModelScope.launch {
            repo.getProductById(productID).catch { _productDetailsState.value = ApiState.Failure(it) }
                .collect {
                    if (it.isSuccessful) {
                        _productDetailsState.value = ApiState.Success(it.body()!!)
                    }
                }
        }
    }

    fun resetState(){
        _productDetailsState.value = ApiState.Loading
    }

}