package com.example.e_commerce.orders.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.model.repo.RepoInterface
import com.example.e_commerce.services.network.ApiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class OrderViewModel(private val repo: RepoInterface) : ViewModel() {

    private val _listOfOrdersMutableStateFlow: MutableStateFlow<ApiState> =
        MutableStateFlow(ApiState.Loading)
    val listOfOrdersStateFlow: StateFlow<ApiState> get() = _listOfOrdersMutableStateFlow

    private val _orderMutableStateFlow: MutableStateFlow<ApiState> =
        MutableStateFlow(ApiState.Loading)
    val ordersStateFlow get() = _orderMutableStateFlow


    fun getCustomerOrders(id: Long) {
        viewModelScope.launch {
            try {
                repo.getCustomerOrders(id)
                    .catch {
                        _listOfOrdersMutableStateFlow.value = ApiState.Failure(it)
                    }
                    .collectLatest {
                        if (it.isSuccessful) {
                            _listOfOrdersMutableStateFlow.value = ApiState.Success(it.body()!!)
                        }
                    }
            } catch (e: SocketTimeoutException) {
                _listOfOrdersMutableStateFlow.value = ApiState.Failure(Throwable("Poor Connection"))
                getCustomerOrders(id)
            }
        }
    }

    fun getOrderById(id: Long) {
        viewModelScope.launch {
            try {
                repo.getOrderById(id)
                    .catch {
                        _orderMutableStateFlow.value = ApiState.Failure(it)
                    }
                    .collectLatest {
                        if (it.isSuccessful) {
                            _orderMutableStateFlow.value = ApiState.Success(it.body()!!)
                        }
                    }
            } catch (e: SocketTimeoutException) {
                _orderMutableStateFlow.value = ApiState.Failure(Throwable("Poor Connection"))
                getOrderById(id)
            }
        }
    }

    fun readFromSp(key: String): String {
        return repo.readStringFromSettingSP(key)
    }

    fun refreshOrderList() {
        _listOfOrdersMutableStateFlow.value = ApiState.Loading
    }

    fun refreshOrderDetailsList() {
        _orderMutableStateFlow.value = ApiState.Loading
    }

}
