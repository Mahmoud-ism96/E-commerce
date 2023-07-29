package com.example.e_commerce.shoppingcart.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.model.pojo.draftorder.send.SendDraftRequest
import com.example.e_commerce.model.pojo.order.OrderData
import com.example.e_commerce.model.repo.RepoInterface
import com.example.e_commerce.services.network.ApiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class CartViewModel(private val repo: RepoInterface) : ViewModel() {

    private val _discountCodesMutableStateFlow: MutableStateFlow<ApiState> =
        MutableStateFlow(ApiState.Loading)
    val discountCodesStateFlow: StateFlow<ApiState> get() = _discountCodesMutableStateFlow

    private val _pricesRulesMutableStateFlow: MutableStateFlow<ApiState> =
        MutableStateFlow(ApiState.Loading)
    val pricesRulesStateFlow: StateFlow<ApiState> get() = _pricesRulesMutableStateFlow

    private val _cartDraftOrderMutableStateFlow: MutableStateFlow<ApiState> =
        MutableStateFlow(ApiState.Loading)
    val cartDraftOrderStateFlow: StateFlow<ApiState> get() = _cartDraftOrderMutableStateFlow

    private val _modifyDraftStatusMutableStateFlow: MutableStateFlow<ApiState> =
        MutableStateFlow(ApiState.Loading)
    val modifyDraftStatusStateFlow: StateFlow<ApiState> get() = _modifyDraftStatusMutableStateFlow

    private val _addressesMutableStateFlow: MutableStateFlow<ApiState> = MutableStateFlow(ApiState.Loading)
    val addressesStateFlow: StateFlow<ApiState> get() = _addressesMutableStateFlow

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

    fun getDraftOrderByDraftId(draftId: Long) {
        viewModelScope.launch {
            repo.getDraftOrderByDraftId(draftId).catch {
                _cartDraftOrderMutableStateFlow.value = ApiState.Failure(it)
            }.collect {
                if (it.isSuccessful) {
                    _cartDraftOrderMutableStateFlow.value = ApiState.Success(it.body()!!)
                }else{
                    _cartDraftOrderMutableStateFlow.value = ApiState.Failure(Throwable(it.code().toString()))
                }
            }
        }
    }

    fun modifyDraftOrder(draft_order_id: Long, draft_order: SendDraftRequest){
        viewModelScope.launch {
            repo.modifyDraftOrder(draft_order_id, draft_order).collect{
                if (it.isSuccessful){
                    _modifyDraftStatusMutableStateFlow.value = ApiState.Success("")
                }
            }
        }
    }

    fun writeStringToSettingSP(key: String, value: String) {
        repo.writeStringToSettingSP(key, value)
    }

    fun readStringFromSettingSP(key: String): String {
        return repo.readStringFromSettingSP(key)
    }

    fun createOrder(order: OrderData) {
        viewModelScope.launch {
            repo.createOrder(order)
        }
    }

    fun getAddressesForCustomer(customer_id: String){
        viewModelScope.launch {
            repo.getAddressesForCustomer(customer_id).catch {
                _addressesMutableStateFlow.value = ApiState.Failure(it)
            }.collect{
                if(it.isSuccessful){
                    _addressesMutableStateFlow.value = ApiState.Success(it.body()!!)
                }else{
                    _addressesMutableStateFlow.value = ApiState.Failure(Throwable(it.code().toString()))
                }
            }
        }
    }


}