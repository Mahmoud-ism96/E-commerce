package com.example.e_commerce.orders.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.model.repo.RepoInterface
import com.example.e_commerce.services.network.ApiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class OrderViewModel(private val repo: RepoInterface) : ViewModel() {

    private val _customerMutableStateFlow: MutableStateFlow<ApiState> =
        MutableStateFlow(ApiState.Loading)
    val customerStateFlow get() = _customerMutableStateFlow

    private val _listOfOrdersMutableStateFlow: MutableStateFlow<ApiState> =
        MutableStateFlow(ApiState.Loading)
    val listOfOrdersStateFlow get() = _listOfOrdersMutableStateFlow

    private val _orderMutableStateFlow: MutableStateFlow<ApiState> =
        MutableStateFlow(ApiState.Loading)
    val ordersStateFlow get() = _listOfOrdersMutableStateFlow

    fun getCustomerId(email: String, name: String) {
        viewModelScope.launch {
            repo.getCustomerByEmailAndName(email, name)
                .catch { _customerMutableStateFlow.value = ApiState.Failure(it) }
                .collectLatest {
                    if (it.isSuccessful) {
                        _customerMutableStateFlow.value = ApiState.Success(it.body()!!)
                    }
                }
        }
    }

    fun getCustomerOrders(id: Long) {
        viewModelScope.launch {
            repo.getCustomerOrders(id)
                .catch {
                    _listOfOrdersMutableStateFlow.value = ApiState.Failure(it)
                }
                .collectLatest {
                    if (it.isSuccessful) {
                        _listOfOrdersMutableStateFlow.value = ApiState.Success(it.body()!!)
                    }
                }

        }
    }

    fun getOrderById(id: Long) {
        viewModelScope.launch {
            repo.getOrderById(id)
                .catch {
                    _orderMutableStateFlow.value = ApiState.Failure(it)
                }
                .collectLatest {
                    if (it.isSuccessful) {
                        _orderMutableStateFlow.value = ApiState.Success(it.body()!!)
                    }
                }
        }
    }


}
