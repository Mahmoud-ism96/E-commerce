package com.example.e_commerce.authentication.viewmodel

import android.util.Log
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
    private val _emailCustomerMutableStateFlow: MutableStateFlow<ApiState> =
        MutableStateFlow(ApiState.Loading)
    val emailCustomerMutableStateFlow: StateFlow<ApiState> get() = _emailCustomerMutableStateFlow

    private val _gmailCustomerMutableStateFlow: MutableStateFlow<ApiState> =
        MutableStateFlow(ApiState.Loading)
    val gmailCustomerMutableStateFlow: StateFlow<ApiState> get() = _gmailCustomerMutableStateFlow

    private val _createDraftStatusMutableStateFlow: MutableStateFlow<ApiState> =
        MutableStateFlow(ApiState.Loading)
    val createDraftStatusStateFlow: StateFlow<ApiState> get() = _createDraftStatusMutableStateFlow

    private val _createGoogleDraftStatusMutableStateFlow: MutableStateFlow<ApiState> =
        MutableStateFlow(ApiState.Loading)
    val createGoogleDraftStatusStateFlow: StateFlow<ApiState> get() = _createGoogleDraftStatusMutableStateFlow

    private val _modifyCustomerMutableStateFlow: MutableStateFlow<ApiState> =
        MutableStateFlow(ApiState.Loading)
    val modifyCustomerMutableStateFlow: StateFlow<ApiState> get() = _modifyCustomerMutableStateFlow

    private val _modifyGoogleCustomerMutableStateFlow: MutableStateFlow<ApiState> =
        MutableStateFlow(ApiState.Loading)
    val modifyGoogleCustomerMutableStateFlow: StateFlow<ApiState> get() = _modifyGoogleCustomerMutableStateFlow

    fun createNewEmailCustomer(customerData: CustomerData) {
        viewModelScope.launch {
            repo.createCustomer(customerData)
                .catch { _emailCustomerMutableStateFlow.value = ApiState.Failure(it) }.collect {
                    if (it.isSuccessful) {
                        _emailCustomerMutableStateFlow.value = ApiState.Success(it.body()!!)
                    }
                }
        }
    }

    fun modifyGoogleCustomer(customerId: Long, customerData: CustomerData) {
        viewModelScope.launch {
            repo.modifyCustomer(customerId, customerData).catch {
                    _modifyGoogleCustomerMutableStateFlow.value = ApiState.Failure(it)
                }.collect {
                    if (it.isSuccessful) {
                        _modifyGoogleCustomerMutableStateFlow.value = ApiState.Success(it.body()!!)
                    } else {
                        _modifyGoogleCustomerMutableStateFlow.value = ApiState.Failure(Throwable(it.code().toString()))
                    }
                }
        }
    }

    fun modifyCustomer(customerId: Long, customerData: CustomerData) {
        viewModelScope.launch {
            repo.modifyCustomer(customerId, customerData)
                .catch { _modifyCustomerMutableStateFlow.value = ApiState.Failure(it) }.collect {
                    if (it.isSuccessful) {
                        _modifyCustomerMutableStateFlow.value = ApiState.Success(it.body()!!)
                        Log.i("TAG", "firebaseAuthWithGoogle: Success")
                    }else{
                        _modifyCustomerMutableStateFlow.value = ApiState.Failure(Throwable(it.code().toString()))
                    }
                }
        }
    }

    fun createGmailEmailCustomer(customerData: CustomerData) {
        viewModelScope.launch {
            repo.createCustomer(customerData).catch {
                _gmailCustomerMutableStateFlow.value = ApiState.Failure(it)
            }.collect {
                if (it.isSuccessful) {
                    _gmailCustomerMutableStateFlow.value = ApiState.Success(it.body()!!)
                } else {
                    _gmailCustomerMutableStateFlow.value =
                        ApiState.Failure(Throwable(it.code().toString()))
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

    fun createGoogleDraftOrder(draft_order: SendDraftRequest) {
        viewModelScope.launch {
            repo.createDraftOrder(draft_order).collectLatest {
                if (it.isSuccessful) {
                    _createGoogleDraftStatusMutableStateFlow.value = ApiState.Success(it.body()!!)
                } else {
                    _createGoogleDraftStatusMutableStateFlow.value =
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