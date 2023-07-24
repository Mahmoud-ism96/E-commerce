package com.example.e_commerce.authentication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.model.pojo.customer.CustomerData
import com.example.e_commerce.model.pojo.draftorder.send.SendDraftRequest
import com.example.e_commerce.model.repo.RepoInterface
import com.example.e_commerce.services.network.ApiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AuthViewModel(private val repo: RepoInterface) : ViewModel() {
    private val _customerMutableStateFlow: MutableStateFlow<ApiState> =
        MutableStateFlow(ApiState.Loading)
    val customerMutableStateFlow: StateFlow<ApiState> get() = _customerMutableStateFlow

    private val _createDraftStatusMutableStateFlow: MutableStateFlow<ApiState> =
        MutableStateFlow(ApiState.Loading)
    val createDraftStatusStateFlow: StateFlow<ApiState> get() = _createDraftStatusMutableStateFlow

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

    fun createDraftOrder(draft_order: SendDraftRequest) {
        viewModelScope.launch {
            repo.createDraftOrder(draft_order).collectLatest {
                if (it.isSuccessful) {
                    _createDraftStatusMutableStateFlow.value = ApiState.Success(it.body()!!)
                } else {
                    _createDraftStatusMutableStateFlow.value =
                        ApiState.Failure(Throwable(it.code().toString()))
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

}