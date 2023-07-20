package com.example.e_commerce.shoppingcart.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.model.repo.RepoInterface
import com.example.e_commerce.services.network.ApiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CartViewModel(private val repo: RepoInterface) : ViewModel() {

    private val _discountCodesMutableStateFlow: MutableStateFlow<ApiState> =
        MutableStateFlow(ApiState.Loading)
    val discountCodesStateFlow: StateFlow<ApiState> get() = _discountCodesMutableStateFlow

    private val _pricesRulesMutableStateFlow: MutableStateFlow<ApiState> =
        MutableStateFlow(ApiState.Loading)
    val pricesRulesStateFlow: StateFlow<ApiState> get() = _pricesRulesMutableStateFlow

    private val _cartItemsMutableStateFlow: MutableStateFlow<ApiState> =
        MutableStateFlow(ApiState.Loading)
    val cartItemsStateFlow: StateFlow<ApiState> get() = _cartItemsMutableStateFlow

    fun getDiscountCodesForPriceRule(priceRuleId: String) {
        viewModelScope.launch {
            repo.getDiscountCodesForPriceRule(priceRuleId).catch {
                _discountCodesMutableStateFlow.value = ApiState.Failure(it)
            }.collect {
                if (it.isSuccessful) {
                    _discountCodesMutableStateFlow.value = ApiState.Success(it.body()!!)
                } else {
                    _discountCodesMutableStateFlow.value =
                        ApiState.Failure(Throwable(it.code().toString()))
                }
            }
        }
    }

    fun getPriceRules() {
        viewModelScope.launch {
            repo.getAllPricesRules().catch {
                _pricesRulesMutableStateFlow.value = ApiState.Failure(it)
            }.collect {
                if (it.isSuccessful) {
                    _pricesRulesMutableStateFlow.value = ApiState.Success(it.body()!!)
                } else {
                    _pricesRulesMutableStateFlow.value =
                        ApiState.Failure(Throwable(it.code().toString()))
                }
            }
        }
    }


    fun getAllShoppingItem() {
        viewModelScope.launch {
            repo.getAllCartItems().collectLatest {
                _cartItemsMutableStateFlow.value = ApiState.Success(it)
            }
        }
    }

    fun updateQuantityForItem(id: Long, quantity: Int) {
        viewModelScope.launch {
            repo.updateQuantity(id, quantity)
        }
    }

    fun deleteItemFromCart(id: Long){
        viewModelScope.launch {
            repo.deleteItemById(id)
        }
    }


}