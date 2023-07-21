package com.example.e_commerce.authentication.signup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.model.pojo.customer.CustomerData
import com.example.e_commerce.model.repo.RepoInterface
import com.example.e_commerce.services.network.ApiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class SignUpViewModel(private val repo: RepoInterface) : ViewModel() {
    private val _customerMutableStateFlow: MutableStateFlow<ApiState> =
        MutableStateFlow(ApiState.Loading)

    val customerMutableStateFlow: StateFlow<ApiState> get() = _customerMutableStateFlow

    fun createNewCustomer(customerData: CustomerData) {
        viewModelScope.launch {
            repo.createCustomer(customerData)
                .catch { _customerMutableStateFlow.value = ApiState.Failure(it) }.collect {
                    if (it.isSuccessful) {
                        _customerMutableStateFlow.value = ApiState.Success(it.body()!!)
                    }
                }
        }
    }

}